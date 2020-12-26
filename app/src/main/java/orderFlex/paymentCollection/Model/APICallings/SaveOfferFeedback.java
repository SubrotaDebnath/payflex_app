package orderFlex.paymentCollection.Model.APICallings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Model.APILog.APILogData;
import orderFlex.paymentCollection.Model.DataBase.DatabaseOperation;
import orderFlex.paymentCollection.Model.OffersListDataClass.OfferPostResponse;
import orderFlex.paymentCollection.Model.OffersListDataClass.OfferResponsePostBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SaveOfferFeedback {
    private String TAG="SaveOfferFeedback";
    private APIinterface apIinterface;
    private Gson gson;
    private Context context;
    private ProgressDialog dialog;
    private BillPaymentResponse billPaymentResponse=null;
    private DatabaseOperation db;
    private APILogData logData=new APILogData();

    public SaveOfferFeedback(Context context) {
        this.context = context;
        db = new DatabaseOperation(context);
    }

    public void pushOfferFeedback(final String username, final String password, OfferResponsePostBody body, final int checkCount, final int totalCount){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Push offer feedback ...");
        dialog.show();
        //////////////log operation///////////
        logData.setCallName("Push Feedback");
        logData.setCallURL(Constant.BASE_URL_PAYFLEX+"SavePaymentData");
        logData.setCallTime(new Helper(context).getDateTimeInEnglish());
        logData.setRequestBody(new Gson().toJson(body));
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
        final Call<OfferPostResponse> billPaymentResponseCall=apIinterface.SaveClientsOfferFeedback(body);
        billPaymentResponseCall.enqueue(new Callback<OfferPostResponse>() {
            @Override
            public void onResponse(Call<OfferPostResponse> call, retrofit2.Response<OfferPostResponse> response) {

                if (totalCount == checkCount){
                    Intent intent = new Intent(context, OrderListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
                //////////////log operation///////////
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setResponseCode(String.valueOf(response.code()));
                    logData.setResponseBody(new Gson().toJson(response.body()));
                    logData.setResponseTime(new Helper(context).getDateTimeInEnglish());
                    db.insertAPILog(logData);
                }
                ///////////////////////////////////
                if (response.isSuccessful()){
                    gson=new Gson();
                    String res= gson.toJson(billPaymentResponse);
                    Log.i(TAG,"Response: "+res);
                    Log.i(TAG,"Response Code: "+response.code());

                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<OfferPostResponse> call, Throwable t) {
                Log.i(TAG,"Faild: "+t.getMessage());
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setException(t.getMessage());
                    db.insertAPILog(logData);
                }

                dialog.cancel();
            }
        });
        return;
    }
}
