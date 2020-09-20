package orderFlex.paymentCollection.Model.SaveOrderData;

import com.google.gson.annotations.SerializedName;

public class SaveOrderDetails {

    @SerializedName("txid")
    private String txID;
    private String product_id;
    private String product_name;
    private String product_type;
    @SerializedName("quantityes")
    private String quantities;
    private String client_id;
    private String taker_id;
    @SerializedName("delevary_date")
    private String delevery_date;
    private String plant;
    @SerializedName("taking_date")
    private String ordered_date;
    private String order_type;

    public SaveOrderDetails(String txID, String product_id, String product_name, String product_type, String quantities, String client_id, String taker_id, String delevery_date, String plant, String ordered_date, String order_type) {
        this.txID = txID;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_type = product_type;
        this.quantities = quantities;
        this.client_id = client_id;
        this.taker_id = taker_id;
        this.delevery_date = delevery_date;
        this.plant = plant;
        this.ordered_date = ordered_date;
        this.order_type = order_type;
    }

    public String getTxID() {
        return txID;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getQuantities() {
        return quantities;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getTaker_id() {
        return taker_id;
    }

    public String getDelevery_date() {
        return delevery_date;
    }

    public String getPlant() {
        return plant;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setTxID(String txID) {
        this.txID = txID;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setTaker_id(String taker_id) {
        this.taker_id = taker_id;
    }

    public void setDelevery_date(String delevery_date) {
        this.delevery_date = delevery_date;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }
}
