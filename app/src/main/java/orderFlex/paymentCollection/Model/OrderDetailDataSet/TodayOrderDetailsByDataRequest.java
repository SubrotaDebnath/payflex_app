package orderFlex.paymentCollection.Model.OrderDetailDataSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOrderDetailsByDataRequest {
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;

    public TodayOrderDetailsByDataRequest(String clientId, String deliveryDate) {
        this.clientId = clientId;
        this.deliveryDate = deliveryDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
