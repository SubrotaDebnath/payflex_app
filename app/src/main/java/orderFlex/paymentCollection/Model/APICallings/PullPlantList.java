package orderFlex.paymentCollection.Model.APICallings;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import orderFlex.paymentCollection.Model.APILog.APILogData;
import orderFlex.paymentCollection.Model.DataBase.DatabaseOperation;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.Model.SaveOrderData.PlantListResponse;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PullPlantList {
    private String TAG="PullPlantList";
    private APIinterface apIinterface;
    private Gson gson;
    private PlantListListener listener;
    private Context context;
    private ProgressDialog dialog;
    private PlantListResponse plantResponse =null;
    private DatabaseOperation db;
    private APILogData logData=new APILogData();

    public PullPlantList(Context context) {
        listener= (PlantListListener) context;
        this.context=context;
        db=new DatabaseOperation(context);
    }
    public void plantListCall(final String username, final String password){
        Log.i(TAG,"Called.....");
//        dialog = new ProgressDialog(context);
//        dialog.setMessage("Updating...");
//        dialog.show();
        //////////////log operation///////////
        logData.setCallName("Plant List");
        logData.setCallURL(Constant.BASE_URL_PAYFLEX+"GetPlants");
        logData.setCallTime(new Helper(context).getDateTimeInEnglish());
        logData.setRequestBody("GET");
        logData.setResponseCode("");
        logData.setResponseBody("");
        logData.setException("");
        /////////////////////////////////
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final String authToken = Credentials.basic(username, password);
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Headers headers = request.headers().newBuilder().add("Authorization", authToken).build();
                        request = request.newBuilder().headers(headers).build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL_PAYFLEX)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apIinterface=retrofit.create(APIinterface.class);
        final Call<PlantListResponse> responseCall = apIinterface.getPlantList();
        responseCall.enqueue(new Callback<PlantListResponse>() {
            @Override
            public void onResponse(Call<PlantListResponse> call, Response<PlantListResponse> response) {
                //////////////log operation///////////
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setResponseCode(String.valueOf(response.code()));
                    logData.setResponseBody(new Gson().toJson(response.body()));
                    logData.setResponseTime(new Helper(context).getDateTimeInEnglish());
                    db.insertAPILog(logData);
                }
                ///////////////////////////////////
                if (response.isSuccessful()){
                    Log.i(TAG,"Response Code: "+response.code());
                    plantResponse =response.body();
                    gson=new Gson();
                    String res=gson.toJson(response.body());
                    Log.i(TAG,"Response: "+res);
                    listener.onPlantListResponse(plantResponse,response.code());
//                    dialog.cancel();
                }
//                dialog.cancel();
            }

            @Override
            public void onFailure(Call<PlantListResponse> call, Throwable t) {
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setException(t.getMessage());
                    db.insertAPILog(logData);
                }
                listener.onPlantListResponse(plantResponse,404);
//                dialog.cancel();
            }
        });
        return;
    }

    public interface PlantListListener{
        void onPlantListResponse(PlantListResponse response, int code);
    }
}
