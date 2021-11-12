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
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillReplaceRequestBody;
import orderFlex.paymentCollection.Utility.Constant;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReplaceBills {
    private String TAG="ReplaceBillAPICall";
    private APIinterface apIinterface;
    private Gson gson;
    private ReplaceBillListener listener;
    private Context context;
    private ProgressDialog dialog;
    private BillPaymentResponse billPaymentResponse=null;
    private DatabaseOperation db;
    private APILogData logData=new APILogData();


    public ReplaceBills(Context context) {
        listener= (ReplaceBillListener) context;
        this.context=context;
        db=new DatabaseOperation(context);
    }

    public void replaceBillCall(final String username, final String password, BillReplaceRequestBody body){
        // preparing interceptor for retrofit
        // interceptor for runtime data checking
        dialog = new ProgressDialog(context);
        dialog.setMessage("Upload todays bills ...");
        dialog.show();
        //////////////log operation///////////
        logData.setCallName("Replace Payment");
        logData.setCallURL(Constant.BASE_URL_PAYFLEX+"ReplacePayment");
        logData.setCallTime(new Helper(context).getDateTimeInEnglish());
        logData.setRequestBody(new Gson().toJson(body));
        logData.setResponseCode("");
        logData.setResponseBody("");
        logData.setException("");
        Log.i(TAG, "Replace Bill: "+new Gson().toJson(body));
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
        final Call<BillPaymentResponse> billPaymentResponseCall=apIinterface.replaceOrderBill(body);
        billPaymentResponseCall.enqueue(new Callback<BillPaymentResponse>() {
            @Override
            public void onResponse(Call<BillPaymentResponse> call, retrofit2.Response<BillPaymentResponse> response) {
                //////////////log operation///////////
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setResponseCode(String.valueOf(response.code()));
                    logData.setResponseBody(new Gson().toJson(response.body()));
                    logData.setResponseTime(new Helper(context).getDateTimeInEnglish());
                    db.insertAPILog(logData);
                }
                ///////////////////////////////////
                if (response.isSuccessful()){
                    billPaymentResponse=response.body();
                    gson=new Gson();
                    String res= gson.toJson(billPaymentResponse);
                    Log.i(TAG,"Login Response: "+res);
                    listener.onReplaceResponse(billPaymentResponse,response.code());
                    dialog.cancel();
                }
                dialog.cancel();
            }
            @Override
            public void onFailure(Call<BillPaymentResponse> call, Throwable t) {
                if (new SharedPrefManager(context).isDebugOn()){
                    logData.setException(t.getMessage());
                    db.insertAPILog(logData);
                }
                listener.onReplaceResponse(billPaymentResponse,404);
                dialog.cancel();
            }
        });
        return;
    }

    public interface ReplaceBillListener{
        void onReplaceResponse(BillPaymentResponse response, int code);
    }
}
