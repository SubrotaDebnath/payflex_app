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
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Utility.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetProductList {
    private String TAG="GetProductList";
    private APIinterface apIinterface;
    private Gson gson;
    private GetProductListListener listener;
    private Context context;
    private ProgressDialog dialog;
    private ProductListResponse paymentListResponse=null;

    public GetProductList(Context context) {
        listener= (GetProductListListener) context;
        this.context=context;
    }

    public void pullProductListCall(final String username, final String password){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Updating...");
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
        final Call<ProductListResponse> paymentListResponseCall = apIinterface.getProducts();
        paymentListResponseCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, retrofit2.Response<ProductListResponse> response) {
                if (response.isSuccessful()){
                    paymentListResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(paymentListResponse);
                    Log.i(TAG,"Product List Response: "+res);
                    listener.onProductListResponse(paymentListResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.i(TAG,t.getMessage());
                listener.onProductListResponse(paymentListResponse,404);
                dialog.cancel();
            }
        });
        return;
    }
    public interface GetProductListListener{
        void onProductListResponse(ProductListResponse response, int code);
    }
}
