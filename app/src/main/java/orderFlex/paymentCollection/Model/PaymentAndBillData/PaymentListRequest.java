package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListRequest {
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("order_code")
    @Expose
    private String orderCode;

    public PaymentListRequest(String clientId, String orderCode) {
        this.clientId = clientId;
        this.orderCode = orderCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
