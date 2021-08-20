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
import orderFlex.paymentCollection.Model.LoginData.LoginClientRequestBody;
import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAPICalling {
    private String TAG="LoginAPICalling";
    private APIinterface apIinterface;
    private Gson gson;
    private LoginListener listener;
    private Context context;
    private ProgressDialog dialog;
    private LoginResponse loginResponse=null;
    private DatabaseOperation db;
    private APILogData logData=new APILogData();

    public LoginAPICalling(Context context) {
        listener= (LoginListener) context;
        this.context=context;
        db=new DatabaseOperation(context);
    }

    public void loginCall(final String username, final String password, LoginClientRequestBody body){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        //////////////log operation///////////
        logData.setCallName("Login");
        logData.setCallURL(Constant.BASE_URL_PAYFLEX+"Client_login");
        logData.setCallTime(new Helper(context).getDateTimeInEnglish());
        logData.setRequestBody(username+";"+password);
        logData.setResponseCode("");
        logData.setResponseBody("");
        logData.setException("");
        /////////////////////////////////
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //generate auth token
        //final String authToken = Credentials.basic(username,password);
        //authentication interceptor
        //LoginRequestBody body=new LoginRequestBody(username,password);
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
        final Call<LoginResponse> loginResponseCall=apIinterface.login(body);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
               Log.i(TAG,"Response Code: "+response.code());
                //////////////log operation///////////
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setResponseCode(String.valueOf(response.code()));
                    logData.setResponseBody(new Gson().toJson(response.body()));
                    logData.setResponseTime(new Helper(context).getDateTimeInEnglish());
                    db.insertAPILog(logData);
                }
                ///////////////////////////////////
                if (response.isSuccessful()){
                    loginResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(loginResponse);
                    Log.i(TAG,"Login Response: "+res);
                    listener.onResponse(loginResponse,response.code());
                }else {
                    listener.onResponse(loginResponse,response.code());
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setException(t.getMessage());
                    db.insertAPILog(logData);
                }
                listener.onResponse(loginResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface LoginListener{
        void onResponse(LoginResponse response,int code);
    }
}