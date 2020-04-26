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
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PullPaymentsList {
    private String TAG="PullPaymentsList";
    private APIinterface apIinterface;
    private Gson gson;
    private PullPaymentsListListener listener;
    private Context context;
    private ProgressDialog dialog;
    private PaymentListResponse paymentListResponse=null;


    public PullPaymentsList(Context context) {
        listener= (PullPaymentsListListener) context;
        this.context=context;
    }

    public void pullPaymentListCall(final String username, final String password, PaymentListRequest paymentListRequest){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Login...");
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
        final Call<PaymentListResponse> paymentListResponseCall = apIinterface.getPayments(paymentListRequest);
        paymentListResponseCall.enqueue(new Callback<PaymentListResponse>() {
            @Override
            public void onResponse(Call<PaymentListResponse> call, retrofit2.Response<PaymentListResponse> response) {
                if (response.isSuccessful()){
                    paymentListResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(paymentListResponse);
                    Log.i(TAG,"Login Response: "+res);
                    listener.onPaymentListResponse(paymentListResponse,response.code());
                    dialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<PaymentListResponse> call, Throwable t) {
                Log.i(TAG,t.getMessage());
                listener.onPaymentListResponse(paymentListResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface PullPaymentsListListener{
        void onPaymentListResponse(PaymentListResponse response, int code);
    }
}
