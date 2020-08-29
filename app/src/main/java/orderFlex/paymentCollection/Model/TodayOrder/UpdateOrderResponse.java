package orderFlex.paymentCollection.Model.TodayOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateOrderResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_amount")
    @Expose
    private String total_amount;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal_amount() {
        return total_amount;
    }
}
