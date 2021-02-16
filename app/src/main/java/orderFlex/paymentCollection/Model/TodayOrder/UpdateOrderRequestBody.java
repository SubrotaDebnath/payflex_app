package orderFlex.paymentCollection.Model.TodayOrder;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderRequestBody {


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

    public UpdateOrderRequestBody(String txID,
                                  String product_id,
                                  String product_name,
                                  String product_type,
                                  String quantities,
                                  String client_id,
                                  String taker_id,
                                  String delevery_date,
                                  String plant,
                                  String ordered_date,
                                  String order_type) {
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


/////////////////////below line created by Subrota
    public void setPlant(String plant) {
        this.plant = plant;
    }
}
