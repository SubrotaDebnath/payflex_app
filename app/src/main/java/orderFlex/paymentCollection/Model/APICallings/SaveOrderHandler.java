package orderFlex.paymentCollection.Model.APICallings;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import orderFlex.paymentCollection.Model.PaymentAndBillData.SaveOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderRequestBody;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaveOrderHandler {
    private String TAG="SaveOrderHandler";
    private APIinterface apIinterface;
    private Gson gson;
    private SaveOrderListener listener;
    private Context context;
    private ProgressDialog dialog;
    private UpdateOrderResponse updateOrderResponse=null;


    public SaveOrderHandler(Context context) {
        listener= (SaveOrderListener) context;
        this.context=context;
    }

    public void pushSaveOrder(final String username, final String password, List<SaveOrderRequest> body){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Saving orders...");
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
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Headers headers = request.headers().newBuilder().add("userName", username).build();
                        request = request.newBuilder().headers(headers).build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Headers headers = request.headers().newBuilder().add("password", password).build();
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
        final Call<UpdateOrderResponse> updateOrderResponseCall=apIinterface.saveNewOrder(body);
        updateOrderResponseCall.enqueue(new Callback<UpdateOrderResponse>() {
            @Override
            public void onResponse(Call<UpdateOrderResponse> call, retrofit2.Response<UpdateOrderResponse> response) {
                if (response.isSuccessful()){
                    updateOrderResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(updateOrderResponse);
                    Log.i(TAG,"Save Response: "+res);
                    Log.i(TAG,"Code:"+response.code());
                    listener.onSaveResponse(updateOrderResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<UpdateOrderResponse> call, Throwable t) {
                listener.onSaveResponse(updateOrderResponse,404);
                dialog.cancel();
            }
        });
        return;
    }
    public interface SaveOrderListener{
        void onSaveResponse(UpdateOrderResponse response,int code);
    }
}
