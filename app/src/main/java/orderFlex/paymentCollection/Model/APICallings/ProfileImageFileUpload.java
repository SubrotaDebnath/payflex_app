package orderFlex.paymentCollection.Model.APICallings;

import android.app.ProgressDialog;
import android.content.Context;
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
import orderFlex.paymentCollection.Model.FileDataClass.FileUploadResponse;
import orderFlex.paymentCollection.Model.UserData.ProImgUploadResponse;
import orderFlex.paymentCollection.Model.UserData.ProfileQueueRequestData;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

/**
 * Created by android on 3/29/2016.
 */
public class ProfileImageFileUpload extends AsyncTask<Void, Void, String> {
    int TIMEOUT_MILLISEC = 30000;
    private Context context;
    private String filename;
    private String fileType;
    private String TAG="ProfileImageFileUpload";
    private String filePath;
    private Helper helper;
    private Gson gson=new Gson();
    private String clientID;
    private String fileDetail;
    private SharedPrefManager prefManager;
    private ProgressDialog dialog;
    private String extension;
    private String sourcePath;
    private boolean isGalleryImg=false;
//    private  ProfileImageListener listener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    public ProfileImageFileUpload(Context context, ProfileQueueRequestData requestData, boolean isGalleryImg) {
        this.context = context;
        helper=new Helper(context);
        this.filename=requestData.getFilename();
        this.fileType=requestData.getFileType();
        this.clientID=requestData.getClientID();
        this.fileDetail=requestData.getFileDetail();
        this.extension=requestData.getExtension();
        this.sourcePath= requestData.getSourcePath();
        this.isGalleryImg=isGalleryImg;

        Log.i(TAG,"File Type: "+fileType);
        prefManager=new SharedPrefManager(context);
        dialog = new ProgressDialog(context);
        dialog.setMessage("Updating Profile Image...");
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

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", content_type)
                        .addFormDataPart("image_type", fileType)
                        .addFormDataPart("trxid", filename)
                        .addFormDataPart("user_id", clientID)
                        .addFormDataPart("file_detail", fileDetail)
                        .addFormDataPart("request_time", helper.getDateTimeInEnglish())
                        .addFormDataPart("extension", extension)
                        .addFormDataPart("username", prefManager.getUsername())
                        .addFormDataPart("password", prefManager.getUserPassword())
                        .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                        .build();
                try{
                    String response=post(Constant.BASE_URL_PAYFLEX+"ProfileImgSave?img="+filename, request_body,sourceFile);
                    Log.i(TAG, "Successful Response: "+response);
                    if (response!=null){
                        Gson gson=new Gson();
                        ProImgUploadResponse uploadResponse=gson.fromJson(response, ProImgUploadResponse.class);
                        Log.i(TAG,"Uploaded File Size: "+uploadResponse.getFileSize());
                        if (uploadResponse.getFileSize()>0 && uploadResponse.getStatus()==202 && uploadResponse.getFileExists()) {
                            Log.i(TAG, "Successfully Updated!!!");
                            if (!isGalleryImg) {
                                sourceFile.delete();
                            }
                            Log.i(TAG, "Img URG: "+uploadResponse.getUrl());
                            prefManager.setProImgUrl(uploadResponse.getUrl());
                            dialog.cancel();
                        }else {
                            Log.i(TAG, "Updated error!!!");
                            dialog.cancel();
                        }
                    }
                }catch (Exception e){
                    Log.i(TAG,"Exception1: " +e.toString());
                    dialog.cancel();
                }
            } catch (Exception ex) {
                Log.i(TAG,"Exception2: " +ex.toString());
                dialog.cancel();
                ex.printStackTrace();
            }
            return null;
        } else
        {
            Log.i(TAG, "no file found");
            dialog.cancel();
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
            return response.body().string();
        }else {
            return null;
        }
    }
}