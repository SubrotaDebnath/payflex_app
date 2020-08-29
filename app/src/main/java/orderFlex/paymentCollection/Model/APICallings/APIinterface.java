package orderFlex.paymentCollection.Model.APICallings;

import java.util.List;

import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.SaveOrderRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.UpdatePaymenResponse;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListRequest;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListResponse;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByCodeRequest;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataRequest;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataResponse;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderRequestBody;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIinterface {
    @POST("Client_login")
    Call<LoginResponse>login();

    @POST("GetClientTodaysOrder")
    Call<TodayOrderDetailsByDataResponse>getTodayOrder(@Body TodayOrderDetailsByDataRequest body);

    @POST("GetOrderDetailByOrderCode")
    Call<TodayOrderDetailsByDataResponse>getOrderByCode(@Body TodayOrderDetailsByCodeRequest body);

    @POST("GetCustomerOrderList")
    Call<CustomerOrderListResponse> getCustomerOrderList(@Body CustomerOrderListRequest body);

    @POST("SavePaymentData")
    Call<BillPaymentResponse>pushOrderBill(@Body BillPaymentRequestBody body);

    @POST("UpdatePaymentData")
    Call<UpdatePaymenResponse>updateOrderPayment(@Body BillPaymentRequestBody body);

    @POST("Payment_methodes")
    Call<PaymentMothodsResponse>getPaymentMethods();

    @POST("update_order")
    Call<UpdateOrderResponse> updateOrder(@Body List<UpdateOrderRequestBody> updateOrderList);

    @POST("Save_order")
    Call<UpdateOrderResponse> saveNewOrder(@Body List<SaveOrderRequest> body);

    @POST("GetOrderPaymentList")
    Call<PaymentListResponse> getPayments(@Body PaymentListRequest body);

    @GET("GetProductDetails")
    Call<ProductListResponse> getProducts();
}
