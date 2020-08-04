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
import orderFlex.paymentCollection.Model.PaymentAndBillData.UpdatePaymenResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateBill {
    private String TAG="LoginAPICalling";
    private APIinterface apIinterface;
    private Gson gson;
    private UpdateBillListener listener;
    private Context context;
    private ProgressDialog dialog;
    private UpdatePaymenResponse updatePaymentResponse=null;


    public UpdateBill(Context context) {
        listener= (UpdateBillListener) context;
        this.context=context;
    }

    public void updateBillCall(final String username, final String password, BillPaymentRequestBody body){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Update today's bills ...");
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
        final Call<UpdatePaymenResponse> billPaymentResponseCall=apIinterface.updateOrderPayment(body);
        billPaymentResponseCall.enqueue(new Callback<UpdatePaymenResponse>() {
            @Override
            public void onResponse(Call<UpdatePaymenResponse> call, retrofit2.Response<UpdatePaymenResponse> response) {
                if (response.isSuccessful()){
                    updatePaymentResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(updatePaymentResponse);
                    Log.i(TAG,"Update Bill Response: "+res);
                    listener.onUpdateResponse(updatePaymentResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<UpdatePaymenResponse> call, Throwable t) {
                listener.onUpdateResponse(updatePaymentResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface UpdateBillListener{
        void onUpdateResponse(UpdatePaymenResponse response,int code);
    }
}
