package orderFlex.paymentCollection.Model.APICallings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
public class ImageFileUploader extends AsyncTask<Void, Void, String> {
    int TIMEOUT_MILLISEC = 30000;
    Context context;
    ProgressDialog dialog;
    private String filename;
    private String fileType;
    private String TAG="ImageFileUploader";
    private String filePath;
    private Helper helper;
    private Gson gson=new Gson();
    private String clientID;
    private String fileDetail;
    private SharedPrefManager prefManager;
    private String extension;
    private String order_code;
    private String payment_id;
    private String sourcePath;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    public ImageFileUploader(Context context, String clientID, String fileDetail, String fileType, String extension, String order_code,String payment_id,String sourcePath) {
        this.context = context;
        helper=new Helper(context);

        this.filename=helper.makeUniqueID();
        this.fileType=fileType;
        this.clientID=clientID;
        this.fileDetail=fileDetail;
        this.extension=extension;
        this.order_code=order_code;
        this.payment_id=payment_id;
        this.sourcePath= sourcePath;

        Log.i(TAG,"File Type: "+fileType);
//        File sampleDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);//directory will be changed
//        filePath= sampleDir.getAbsolutePath();
//        Log.i(TAG,"Path:"+filePath);

        prefManager=new SharedPrefManager(context);
        dialog = new ProgressDialog(context);
//        dialog.setMessage("Uploading file....");
//        dialog.show();
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
//        String sourceFileUri = filePath+"/"+filename+".jpg";
//        String sourceFileDirectory = filePath+"/";
//
//        Log.i(TAG, "uri :"+sourceFileUri);
//        File sourceFile = new File(sourceFileUri);
        File sourceFile = new File(sourcePath);
        //Database db=new Database(context);
            //********get the file *********
//        try {
//            File sourceDirectory = new File(sourceFileDirectory);
//            for (File ff : sourceDirectory.listFiles()) {
//                if (ff.isFile())
//                    Log.i(TAG, "File Name:" + ff.getName());
//                if (ff.getName().toString().contains(filename)) {
////                    sourceFileUri = "/mnt/sdcard/onuRecords/" + ff.getName();
//                    sourceFileUri = filePath+"/" + ff.getName();
//                    sourceFile = new File(sourceFileUri);
//                }
//            }
//        }catch (Exception e)
//        {
//            Log.i(TAG, "Exception :" + e);
//        }
            //******** file found ***********

//        Log.i(TAG, "Src URI :" + sourceFileUri);
        Log.i(TAG, "Activity src URI: "+sourcePath);

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
                    Log.i(TAG,"File path: "+file_path);
                    Log.i(TAG, "Filename:" + filename);
                    Log.i(TAG,"Order Code: "+order_code);

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
                    try{
                        String response = post(Constant.BASE_URL_PAYFLEX+"ImgFileSave?img="+filename, request_body);
                        //String response = post(info.getUrl()+"/callRecordDemo?audio="+filename, request_body);
                        if (response!=null){
                            Log.i(TAG, "Successful: "+response);
                            Gson gson=new Gson();
                            FileUploadResponse uploadResponse=gson.fromJson(response, FileUploadResponse.class);
                            Log.i(TAG,"Uploaded File Size: "+uploadResponse.getFileSize());
                            if (uploadResponse.getFileSize()>0 && uploadResponse.getStatus()==4000 && uploadResponse.getFileExists()){
                                Log.i(TAG,"Successfully Updated!!!");
                                //db.updateCallQueueForAudioUp(filename,"1");
                                sourceFile.delete();
                                Intent intent = new Intent(context, OrderDetailsActivity.class);
                                context.startActivity(intent);
//                                dialog.cancel();
                            }
//                            dialog.cancel();
                        }else {
//                            dialog.cancel();
                            Log.i(TAG,"Not successful!!!");
                            sourceFile.delete();
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Exception1: " +e.toString());
//                        dialog.cancel();

                    }
                } catch (Exception ex) {
                    Log.i(TAG,"Exception2: " +ex.toString());
                    ex.printStackTrace();
//                    dialog.cancel();
                }
                return null;
            } else
            {
                Log.i(TAG, "no file found");
                //db.updateCallQueueForAudioUp(filename,"2");
                //db.deleteCall(filename);
//                dialog.cancel();
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
            Log.i(TAG, "Response: "+response.body().string());
            return response.body().string();

        }else {
            Log.i(TAG,response.message());
            Log.i(TAG, "Response: "+response.body().string());
//            dialog.cancel();
            return null;
        }

    }
}