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
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PushBills {
    private String TAG="LoginAPICalling";
    private APIinterface apIinterface;
    private Gson gson;
    private PushBillListener listener;
    private Context context;
    private ProgressDialog dialog;
    private BillPaymentResponse billPaymentResponse=null;


    public PushBills(Context context) {
        listener= (PushBillListener) context;
        this.context=context;
    }

    public void pushBillCall(final String username, final String password, BillPaymentRequestBody body){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Upload todays bills ...");
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
        final Call<BillPaymentResponse> billPaymentResponseCall=apIinterface.pushOrderBill(body);
        billPaymentResponseCall.enqueue(new Callback<BillPaymentResponse>() {
            @Override
            public void onResponse(Call<BillPaymentResponse> call, retrofit2.Response<BillPaymentResponse> response) {
                if (response.isSuccessful()){
                    billPaymentResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(billPaymentResponse);
                    Log.i(TAG,"Login Response: "+res);
                    listener.onResponse(billPaymentResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<BillPaymentResponse> call, Throwable t) {
                listener.onResponse(billPaymentResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface PushBillListener{
        void onResponse(BillPaymentResponse response,int code);
    }
}
