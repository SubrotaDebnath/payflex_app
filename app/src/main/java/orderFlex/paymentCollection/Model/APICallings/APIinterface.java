package orderFlex.paymentCollection.Model.APICallings;

import java.util.List;

import orderFlex.paymentCollection.Model.LoginData.LoginRequestBody;
import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.Model.TodayOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrderResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIinterface {
    @POST("Client_login")
    Call<LoginResponse>login();

    @POST("GetClientTodaysOrder")
    Call<TodayOrderResponse>getTodayOrder(@Body TodayOrderRequest body);
}
