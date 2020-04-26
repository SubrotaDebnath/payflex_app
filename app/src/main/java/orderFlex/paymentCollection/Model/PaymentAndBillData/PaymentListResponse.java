package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentListResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("payment_list")
    @Expose
    private List<PaymentList> paymentList = null;

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

    public List<PaymentList> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentList> paymentList) {
        this.paymentList = paymentList;
    }
    public class PaymentList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("trxid")
        @Expose
        private String trxid;
        @SerializedName("payment_mode_id")
        @Expose
        private String paymentModeId;
        @SerializedName("financial_institution_id")
        @Expose
        private String financialInstitutionId;
        @SerializedName("payment_date_time")
        @Expose
        private String paymentDateTime;
        @SerializedName("reference_no")
        @Expose
        private String referenceNo;
        @SerializedName("image_id")
        @Expose
        private String imageId;
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
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

        public String getTrxid() {
            return trxid;
        }

        public void setTrxid(String trxid) {
            this.trxid = trxid;
        }

        public String getPaymentModeId() {
            return paymentModeId;
        }

        public void setPaymentModeId(String paymentModeId) {
            this.paymentModeId = paymentModeId;
        }

        public String getFinancialInstitutionId() {
            return financialInstitutionId;
        }

        public void setFinancialInstitutionId(String financialInstitutionId) {
            this.financialInstitutionId = financialInstitutionId;
        }

        public String getPaymentDateTime() {
            return paymentDateTime;
        }

        public void setPaymentDateTime(String paymentDateTime) {
            this.paymentDateTime = paymentDateTime;
        }

        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
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
}
