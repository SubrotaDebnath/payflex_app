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
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListRequest;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListResponse;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PullCustomerOrderList {
    private String TAG="PullCustomerOrderList";
    private APIinterface apIinterface;
    private Gson gson;
    private OrderListListener listener;
    private Context context;
    private ProgressDialog dialog;
    private CustomerOrderListResponse orderListResponse=null;
    private DatabaseOperation db;
    private APILogData logData=new APILogData();

    public PullCustomerOrderList(Context context) {
        listener= (OrderListListener) context;
        db=new DatabaseOperation(context);
        this.context=context;
    }

    public void pullCustomerOrderListCall(final String username, final String password, CustomerOrderListRequest orderRequest){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Customer Order pull...");
        dialog.show();
        //////////////log operation///////////
        logData.setCallName("Order List");
        logData.setCallURL(Constant.BASE_URL_PAYFLEX+"GetCustomerOrderList");
        logData.setCallTime(new Helper(context).getDateTimeInEnglish());
        logData.setRequestBody(new Gson().toJson(orderRequest));
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
        final Call<CustomerOrderListResponse> orderResponseCall = apIinterface.getCustomerOrderList(orderRequest);
        orderResponseCall.enqueue(new Callback<CustomerOrderListResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderListResponse> call, retrofit2.Response<CustomerOrderListResponse> response) {
                //////////////log operation///////////
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setResponseCode(String.valueOf(response.code()));
                    logData.setResponseBody(new Gson().toJson(response.body()));
                    logData.setResponseTime(new Helper(context).getDateTimeInEnglish());
                    db.insertAPILog(logData);
                }
                ///////////////////////////////////

                if (response.isSuccessful()){
                    orderListResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(orderListResponse);
                    Log.i(TAG,"Login Response: "+res);
                    listener.onCustomerOrderListResponse(orderListResponse,response.code());
                    dialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<CustomerOrderListResponse> call, Throwable t) {
                Log.i(TAG,t.getMessage());
                //////////////log operation///////////
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setException(t.getMessage());
                    db.insertAPILog(logData);
                }
                ///////////////////////////////////////
                listener.onCustomerOrderListResponse(orderListResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface OrderListListener{
        void onCustomerOrderListResponse(CustomerOrderListResponse response, int code);
    }
}
