package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("product_list")
    @Expose
    private List<ProductList> productList = null;

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

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }
    public class ProductList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("p_name")
        @Expose
        private String pName;
        @SerializedName("p_type")
        @Expose
        private String pType;
        @SerializedName("p_discription")
        @Expose
        private Object pDiscription;
        @SerializedName("last_updated_price")
        @Expose
        private Object lastUpdatedPrice;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("p_retailPrice")
        @Expose
        private String pRetailPrice;
        @SerializedName("p_wholesalePrice")
        @Expose
        private String pWholesalePrice;
        @SerializedName("p_specialPrice")
        @Expose
        private String pSpecialPrice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public Object getPDiscription() {
            return pDiscription;
        }

        public void setPDiscription(Object pDiscription) {
            this.pDiscription = pDiscription;
        }

        public Object getLastUpdatedPrice() {
            return lastUpdatedPrice;
        }

        public void setLastUpdatedPrice(Object lastUpdatedPrice) {
            this.lastUpdatedPrice = lastUpdatedPrice;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

    }
}
