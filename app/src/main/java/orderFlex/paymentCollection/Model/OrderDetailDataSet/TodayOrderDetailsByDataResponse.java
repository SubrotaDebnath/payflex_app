package orderFlex.paymentCollection.Model.OrderDetailDataSet;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayOrderDetailsByDataResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_details")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public class OrderDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("taking_date")
        @Expose
        private String takingDate;
        @SerializedName("delivery_date")
        @Expose
        private String deliveryDate;
        @SerializedName("insert_date_time")
        @Expose
        private String insertDateTime;
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("taker_id")
        @Expose
        private String takerId;
        @SerializedName("order_for_client_id")
        @Expose
        private String orderForClientId;
        @SerializedName("txid")
        @Expose
        private String txid;
        @SerializedName("product_order_id")
        @Expose
        private String productOrderId;
        @SerializedName("order_type")
        @Expose
        private String orderType;
        @SerializedName("plant")
        @Expose
        private String plant;
        @SerializedName("plantName")
        @Expose
        private String plantName;
        @SerializedName("quantityes")
        @Expose
        private String quantityes;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("p_name")
        @Expose
        private String pName;
        @SerializedName("p_type")
        @Expose
        private String pType;
        @SerializedName("p_retailPrice")
        @Expose
        private String pRetailPrice;
        @SerializedName("p_wholesalePrice")
        @Expose
        private String pWholesalePrice;
        @SerializedName("p_specialPrice")
        @Expose
        private String pSpecialPrice;
        @SerializedName("p_discription")
        @Expose
        private Object pDiscription;

        public String getPlantName() {
            return plantName;
        }

        public String getpRetailPrice() {
            return pRetailPrice;
        }

        public void setpRetailPrice(String pRetailPrice) {
            this.pRetailPrice = pRetailPrice;
        }

        public String getpWholesalePrice() {
            return pWholesalePrice;
        }

        public void setpWholesalePrice(String pWholesalePrice) {
            this.pWholesalePrice = pWholesalePrice;
        }

        public String getpSpecialPrice() {
            return pSpecialPrice;
        }

        public void setpSpecialPrice(String pSpecialPrice) {
            this.pSpecialPrice = pSpecialPrice;
        }

        public Object getpDiscription() {
            return pDiscription;
        }

        public void setpDiscription(Object pDiscription) {
            this.pDiscription = pDiscription;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTakingDate() {
            return takingDate;
        }

        public void setTakingDate(String takingDate) {
            this.takingDate = takingDate;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getInsertDateTime() {
            return insertDateTime;
        }

        public void setInsertDateTime(String insertDateTime) {
            this.insertDateTime = insertDateTime;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getTakerId() {
            return takerId;
        }

        public void setTakerId(String takerId) {
            this.takerId = takerId;
        }

        public String getOrderForClientId() {
            return orderForClientId;
        }

        public void setOrderForClientId(String orderForClientId) {
            this.orderForClientId = orderForClientId;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getProductOrderId() {
            return productOrderId;
        }

        public void setProductOrderId(String productOrderId) {
            this.productOrderId = productOrderId;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getPlant() {
            return plant;
        }

        public void setPlant(String plant) {
            this.plant = plant;
        }

        public String getQuantityes() {
            return quantityes;
        }

        public void setQuantityes(String quantityes) {
            this.quantityes = quantityes;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public String getPType() {
            return pType;
        }

        public void setPType(String pType) {
            this.pType = pType;
        }

        public String getPRetailPrice() {
            return pRetailPrice;
        }

        public void setPRetailPrice(String pRetailPrice) {
            this.pRetailPrice = pRetailPrice;
        }

        public String getPWholesalePrice() {
            return pWholesalePrice;
        }

        public void setPWholesalePrice(String pWholesalePrice) {
            this.pWholesalePrice = pWholesalePrice;
        }

        public String getPSpecialPrice() {
            return pSpecialPrice;
        }

        public void setPSpecialPrice(String pSpecialPrice) {
            this.pSpecialPrice = pSpecialPrice;
        }

        public Object getPDiscription() {
            return pDiscription;
        }

        public void setPDiscription(Object pDiscription) {
            this.pDiscription = pDiscription;
        }

    }
}
