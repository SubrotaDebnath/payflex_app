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
import orderFlex.paymentCollection.Model.DataBase.DatabaseOperation;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentQueueRequestData;
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
    private Context context;
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
    private DatabaseOperation db;
    private boolean isGalleryImg=false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    public ImageFileUploader(Context context, PaymentQueueRequestData requestData,boolean isGalleryImg) {
        this.context = context;
        helper=new Helper(context);
        db=new DatabaseOperation(context);

        this.filename=requestData.getFilename();
        this.fileType=requestData.getFileType();
        this.clientID=requestData.getClientID();
        this.fileDetail=requestData.getFileDetail();
        this.extension=requestData.getExtension();
        this.order_code=requestData.getOrder_code();
        this.payment_id=requestData.getPayment_id();
        this.sourcePath= requestData.getSourcePath();
        this.isGalleryImg=isGalleryImg;

        Log.i(TAG,"File Type: "+fileType);

        prefManager=new SharedPrefManager(context);
        dialog = new ProgressDialog(context);
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
        File sourceFile = new File(sourcePath);
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
                            .addFormDataPart("request_time", helper.getDateTimeInEnglish())
                            .addFormDataPart("extension", extension)
                            .addFormDataPart("order_code",order_code)
                            .addFormDataPart("payment_id",payment_id)
                            .addFormDataPart("username", prefManager.getUsername())
                            .addFormDataPart("password", prefManager.getUserPassword())
                            .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                            .build();
                    try{
                        String response = post(Constant.BASE_URL_PAYFLEX+"ImgFileSave?img="+filename, request_body,sourceFile);
                        //String response = post(info.getUrl()+"/callRecordDemo?audio="+filename, request_body);
                        if (response!=null){
                            dbUpdateOnResponse(response,sourceFile);
                        }else {
//                            dialog.cancel();
                            db.updateImgQueue(filename,"0");
                            Log.i(TAG,"Not successful!!!");
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Exception1: " +e.toString());
//                        db.updateImgQueue(filename,"0");
//                        dialog.cancel();
                    }
                } catch (Exception ex) {
                    Log.i(TAG,"Exception2: " +ex.toString());
                    db.updateImgQueue(filename,"0");
                    ex.printStackTrace();
//                    dialog.cancel();
                }
                return null;
            } else
            {
                Log.i(TAG, "no file found");
                db.deleteIMGQueueByPaymentID(filename);
//                dialog.cancel();
            }
        return null;
    }

    public String post(String url, RequestBody body,File sourceFile) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()){
            Log.i(TAG, "Response: "+response.body().string());
//            dbUpdateOnResponse(response.body().string(),sourceFile);
            return response.body().string();

        }else {
            Log.i(TAG, "Failed Response: "+response.body().string());
//            dialog.cancel();
            return null;
        }

    }
    private void dbUpdateOnResponse(String response,File sourceFile){
        Log.i(TAG, "Successful: "+response);
        Gson gson=new Gson();
        FileUploadResponse uploadResponse=gson.fromJson(response, FileUploadResponse.class);
        Log.i(TAG,"Uploaded File Size: "+uploadResponse.getFileSize());
        if (uploadResponse.getFileSize()>0 && uploadResponse.getStatus()==4000 && uploadResponse.getFileExists()){
            Log.i(TAG,"Successfully Updated!!!");
            db.updateImgQueue(filename,"1");
            if (!isGalleryImg){
                sourceFile.delete();
            }

//      dialog.cancel();
        }else {
            db.updateImgQueue(filename,"0");
        }
//  dialog.cancel();
    }
}