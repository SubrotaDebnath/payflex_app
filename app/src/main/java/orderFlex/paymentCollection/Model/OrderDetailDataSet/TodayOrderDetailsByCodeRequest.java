package orderFlex.paymentCollection.Model.OrderDetailDataSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOrderDetailsByCodeRequest {
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("order_code")
    @Expose
    private String order_code;

    public TodayOrderDetailsByCodeRequest(String clientId, String order_code) {
        this.clientId = clientId;
        this.order_code = order_code;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
}
