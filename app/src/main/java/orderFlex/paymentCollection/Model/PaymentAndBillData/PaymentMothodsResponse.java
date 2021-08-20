package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentMothodsResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("bank_list")
    @Expose
    private List<BankList> bankList = null;
    @SerializedName("Payment_methode")
    @Expose
    private List<PaymentMethode> paymentMethode = null;

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

    public List<BankList> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankList> bankList) {
        this.bankList = bankList;
    }

    public List<PaymentMethode> getPaymentMethode() {
        return paymentMethode;
    }

    public void setPaymentMethode(List<PaymentMethode> paymentMethode) {
        this.paymentMethode = paymentMethode;
    }

    public class PaymentMethode {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("methode_name")
        @Expose
        private String methodeName;
        @SerializedName("custom_methode")
        @Expose
        private Object customMethode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMethodeName() {
            return methodeName;
        }

        public void setMethodeName(String methodeName) {
            this.methodeName = methodeName;
        }

        public Object getCustomMethode() {
            return customMethode;
        }

        public void setCustomMethode(Object customMethode) {
            this.customMethode = customMethode;
        }

    }

    public class BankList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("bank_name")
        @Expose
        private String bankName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

    }
}
