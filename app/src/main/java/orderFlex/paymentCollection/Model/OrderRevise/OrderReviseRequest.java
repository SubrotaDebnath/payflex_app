package orderFlex.paymentCollection.Model.OrderRevise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderReviseRequest {
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public OrderReviseRequest(String orderCode, String userId) {
        this.orderCode = orderCode;
        this.userId = userId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
