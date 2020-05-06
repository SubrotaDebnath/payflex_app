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
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PullPaymentMethods {
    private String TAG="PullPaymentMethods";
    private APIinterface apIinterface;
    private Gson gson;
    private PaymentMethodsListener listener;
    private Context context;
    private ProgressDialog dialog;
    private PaymentMothodsResponse paymentMothodsResponse=null;

    public PullPaymentMethods(Context context) {
        listener= (PaymentMethodsListener) context;
        this.context=context;
    }
    public void paymentMethodsCall(final String username, final String password){
        Log.i(TAG,"Called.....");
        dialog = new ProgressDialog(context);
        dialog.setMessage("Preparing...");
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
        final Call<PaymentMothodsResponse> responseCall = apIinterface.getPaymentMethods();
        responseCall.enqueue(new Callback<PaymentMothodsResponse>() {
            @Override
            public void onResponse(Call<PaymentMothodsResponse> call, Response<PaymentMothodsResponse> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Response Code: "+response.code());
                    paymentMothodsResponse=response.body();
                    gson=new Gson();
                    String res=gson.toJson(response.body());
                    Log.i(TAG,"Response: "+res);
                    listener.onPreResponse(paymentMothodsResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(Call<PaymentMothodsResponse> call, Throwable t) {
                listener.onPreResponse(paymentMothodsResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface PaymentMethodsListener{
        void onPreResponse(PaymentMothodsResponse response, int code);
    }
}
