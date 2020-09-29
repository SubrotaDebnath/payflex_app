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
import orderFlex.paymentCollection.Model.OrderRevise.OrderReviseRequest;
import orderFlex.paymentCollection.Model.OrderRevise.OrderReviseResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderReviseSubmit {
    private String TAG="LoginAPICalling";
    private APIinterface apIinterface;
    private Gson gson;
    private OrderReviseListener listener;
    private Context context;
    private ProgressDialog dialog;
    private OrderReviseResponse reviseResponse=null;


    public OrderReviseSubmit(Context context) {
        listener= (OrderReviseListener) context;
        this.context=context;
    }

    public void reviseSubmitCall(final String username, final String password, OrderReviseRequest body){
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
        final Call<OrderReviseResponse> billPaymentResponseCall=apIinterface.orderRevise(body);
        billPaymentResponseCall.enqueue(new Callback<OrderReviseResponse>() {
            @Override
            public void onResponse(Call<OrderReviseResponse> call, retrofit2.Response<OrderReviseResponse> response) {
                if (response.isSuccessful()){
                    reviseResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(reviseResponse);
                    Log.i(TAG,"Update Bill Response: "+res);
                    listener.onReviseResponse(reviseResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<OrderReviseResponse> call, Throwable t) {
                listener.onReviseResponse(reviseResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface OrderReviseListener{
        void onReviseResponse(OrderReviseResponse response,int code);
    }
}
