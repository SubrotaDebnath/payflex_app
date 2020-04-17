package orderFlex.paymentCollection.Model.APICallings;

import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIinterface {
    @POST("Client_login")
    Call<LoginResponse>login();

    @POST("GetClientTodaysOrder")
    Call<TodayOrderResponse>getTodayOrder(@Body TodayOrderRequest body);

    @POST("SavePaymentData")
    Call<BillPaymentResponse>pushOrderBill(@Body BillPaymentRequestBody body);

    @POST("Payment_methodes")
    Call<PaymentMothodsResponse>getPaymentMethods();
}
