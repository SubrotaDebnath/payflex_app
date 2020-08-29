package orderFlex.paymentCollection.Model.APICallings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import orderFlex.paymentCollection.OrderDetailsActivity.OrderDetailsActivity;
import orderFlex.paymentCollection.Model.FileDataClass.FileUploadResponse;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

/**
 * Created by android on 3/29/2016.
 */
public class FileUploader extends AsyncTask<Void, Void, String> {
    int TIMEOUT_MILLISEC = 30000;
    Context context;
    ProgressDialog dialog;
    private String filename;
    private String fileType;
    private String TAG="FileUploader";
    private String filePath;
    private Helper helper;
    private Gson gson=new Gson();
    private String clientID;
    private String fileDetail;
    private SharedPrefManager prefManager;
    private String extension;
    private String order_code;
    private String payment_id;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    public FileUploader(Context context, String clientID, String name, String fileDetail, String fileType, String extension,String order_code,String payment_id) {
        this.context = context;
        this.filename=name;
        this.fileType=fileType;
        this.clientID=clientID;
        this.fileDetail=fileDetail;
        this.extension=extension;
        this.order_code=order_code;
        this.payment_id=payment_id;

        Log.i(TAG,"Con_Call Type: "+fileType);
        File sampleDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);//directory will be changed
        filePath= sampleDir.getAbsolutePath();
        Log.i(TAG,"Path:"+filePath);
        helper=new Helper(context);
        prefManager=new SharedPrefManager(context);
        dialog = new ProgressDialog(context);
        dialog.setMessage("Uploading file....");
        dialog.show();
    }
    @Override
    protected void onPostExecute(String result) {
       // Log.i(TAG,"onPost: "+result);
    }
    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... params){
        String sourceFileUri = filePath+"/"+filename+".jpg";
        String sourceFileDirectory = filePath+"/";

        Log.i(TAG, "uri :"+sourceFileUri);
        File sourceFile = new File(sourceFileUri);
        //Database db=new Database(context);
            //********get the file *********
        try {
            File sourceDirectory = new File(sourceFileDirectory);
            for (File ff : sourceDirectory.listFiles()) {
                if (ff.isFile())
                    Log.i(TAG, "File Name:" + ff.getName());
                if (ff.getName().toString().contains(filename)) {
//                    sourceFileUri = "/mnt/sdcard/onuRecords/" + ff.getName();
                    sourceFileUri = filePath+"/" + ff.getName();
                    sourceFile = new File(sourceFileUri);
                }
            }
        }catch (Exception e)
        {
            Log.i(TAG, "Exception :" + e);
        }
            //******** file found ***********

        Log.i(TAG, "Src URI :" + sourceFileUri);
            if (sourceFile.isFile()) {
                try {
                    //File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type = getMimeType(sourceFile.getPath());
                    Log.i(TAG, "Content Type:" + content_type);

                    String file_path = sourceFile.getAbsolutePath();
                    OkHttpClient client;
                    client = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), sourceFile);
                    Log.i(TAG, "Filename:" + filename);
                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type", content_type)
                            .addFormDataPart("image_type", "2")
                            .addFormDataPart("trxid", filename)
                            .addFormDataPart("user_id", clientID)
                            .addFormDataPart("file_detail", fileDetail)
                            .addFormDataPart("request_time", helper.getDateTime())
                            .addFormDataPart("extension", extension)
                            .addFormDataPart("order_code",order_code)
                            .addFormDataPart("payment_id",payment_id)
                            .addFormDataPart("username", prefManager.getUsername())
                            .addFormDataPart("password", prefManager.getUserPassword())
                            .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                            .build();


//                    Request request = new Request.Builder()
////                           //.url("http://tester.onuserver.com/callRecorder/")
////                            .url(info.getUrl()+"/callRecordSave?audio="+filename)
////                            .post(request_body)
////                            .build();
                    //Log.i(TAG,"Request body: "+request_body.toString());
                    //new test
                    try{
                        String response = post(Constant.BASE_URL_PAYFLEX+"ImgFileSave?audio="+filename, request_body);
                        //String response = post(info.getUrl()+"/callRecordDemo?audio="+filename, request_body);
                        if (response!=null){
                            Log.i(TAG, "Successful: "+response);
                            Gson gson=new Gson();
                            FileUploadResponse uploadResponse=gson.fromJson(response, FileUploadResponse.class);
                            Log.i(TAG,"Uploaded File Size: "+uploadResponse.getFileSize());
                            if (uploadResponse.getFileSize()>0 && uploadResponse.getStatus()==4000 && uploadResponse.getFileExists()){
                                Log.i(TAG,"Successfully Updated!!!");
                                //db.updateCallQueueForAudioUp(filename,"1");
                                //sourceFile.delete();
                                Intent intent = new Intent(context, OrderDetailsActivity.class);
//                                intent.putExtra("order_type", Constants.FORECAST_ORDER);
//                                intent.putExtra("OFFICER_CODDED_ID", prefManager.getCodedId());
//                                intent.putExtra("CURRENT_DATE", new Helper(context).getYearMonthDate());
                                context.startActivity(intent);
                                dialog.cancel();
                            }
                        }else {
                            dialog.cancel();
                            Log.i(TAG,"Not successful!!!");
                        }
                    }catch (Exception e){

                    }
                    //new test end
//                    Response response = client.newCall(request).execute();
//                    //Log.i(TAG,"Request: "+"type: "+ content_type+"calltype: "+ CallType+"trxid: "+ filename+"device_id"+ info.getImei());
//                    try{
//                        //Response response = client.newCall(request).execute();
//                        //Log.i(TAG, "Error : " + response);
//                        if (!response.isSuccessful()) {
//                            Log.i(TAG, "Error Response: " + response.body().string());
//                            throw new IOException("Error : " + response);
//                        }
//                        else {
//                            Log.i(TAG, "Successful Response :" + response.body().string());
//                            String res="";
//                            db.updateCallQueueForAudioUp(filename,"1");
//                            sourceFile.delete();
//                            //return response.body().string();
//                        }
//                    } catch (IOException e) {
//                        Log.i(TAG, "Exception: "+e.toString());
//                        e.printStackTrace();
//                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            } else
            {
                Log.i(TAG, "no file found");
                //db.updateCallQueueForAudioUp(filename,"2");
                //db.deleteCall(filename);
            }
        return null;
    }

    public String post(String url, RequestBody body) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            Log.i(TAG,"Successful Response: "+response.body().string());
            return response.body().string();
        }else {
            Log.i(TAG,"Failed Response: "+response.body().string());
            return null;
        }

    }
}