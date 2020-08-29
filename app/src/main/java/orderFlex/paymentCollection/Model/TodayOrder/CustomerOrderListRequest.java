package orderFlex.paymentCollection.Model.TodayOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerOrderListRequest {
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;

    public CustomerOrderListRequest(String clientId, String start_date, String end_date) {
        this.clientId = clientId;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
