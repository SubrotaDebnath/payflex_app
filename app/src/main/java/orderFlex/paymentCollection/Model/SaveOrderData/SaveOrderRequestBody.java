package orderFlex.paymentCollection.Model.SaveOrderData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveOrderRequestBody {
    @SerializedName("taking_date")
    @Expose
    private String taking_date;
    @SerializedName("delivery_date")
    @Expose
    private String delivery_date;
    @SerializedName("order_code")
    @Expose
    private String order_code;
    @SerializedName("trxid")
    @Expose
    private String trxid;
    @SerializedName("taker_id")
    @Expose
    private String taker_id;
    @SerializedName("order_for_client_id")
    @Expose
    private String order_for_client_id;
    @SerializedName("order_detail")
    @Expose
    private List<SaveOrderDetails> order_details;

    public SaveOrderRequestBody(String taking_date, String delivery_date, String order_code, String trxid, String taker_id, String order_for_client_id, List<SaveOrderDetails> order_details) {
        this.taking_date = taking_date;
        this.delivery_date = delivery_date;
        this.order_code = order_code;
        this.trxid = trxid;
        this.taker_id = taker_id;
        this.order_for_client_id = order_for_client_id;
        this.order_details = order_details;
    }

    public String getTrxid() {
        return trxid;
    }

    public void setTrxid(String trxid) {
        this.trxid = trxid;
    }

    public String getTaking_date() {
        return taking_date;
    }

    public void setTaking_date(String taking_date) {
        this.taking_date = taking_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTaker_id() {
        return taker_id;
    }

    public void setTaker_id(String taker_id) {
        this.taker_id = taker_id;
    }

    public String getOrder_for_client_id() {
        return order_for_client_id;
    }

    public void setOrder_for_client_id(String order_for_client_id) {
        this.order_for_client_id = order_for_client_id;
    }

    public List<SaveOrderDetails> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<SaveOrderDetails> order_details) {
        this.order_details = order_details;
    }
}
