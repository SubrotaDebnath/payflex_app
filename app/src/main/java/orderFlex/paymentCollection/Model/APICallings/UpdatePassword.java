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
import orderFlex.paymentCollection.Model.UserData.PassChangeReqBody;
import orderFlex.paymentCollection.Model.UserData.PassChangeResponseBody;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdatePassword {
    private String TAG="UpdatePassword";
    private APIinterface apIinterface;
    private Gson gson;
    private PassChangeListener listener;
    private Context context;
    private ProgressDialog dialog;
    private PassChangeResponseBody passChangeResponse =null;


    public UpdatePassword(Context context) {
        listener= (PassChangeListener) context;
        this.context=context;
    }

    public void updatePassCall(final String username, final String password, PassChangeReqBody body){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Update user password bills ...");
        dialog.show();
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
        final Call<PassChangeResponseBody> billPaymentResponseCall=apIinterface.passChange(body);
        billPaymentResponseCall.enqueue(new Callback<PassChangeResponseBody>() {
            @Override
            public void onResponse(Call<PassChangeResponseBody> call, retrofit2.Response<PassChangeResponseBody> response) {
                if (response.isSuccessful()){
                    passChangeResponse =response.body();
                    gson=new Gson();
                    String res= gson.toJson(passChangeResponse);
                    Log.i(TAG,"Login Response: "+res);
                    listener.onPasswordUpdateResponse(passChangeResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<PassChangeResponseBody> call, Throwable t) {
                listener.onPasswordUpdateResponse(passChangeResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface PassChangeListener{
        void onPasswordUpdateResponse(PassChangeResponseBody response,int code);
    }
}
