package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillReplaceRequestBody {
    @SerializedName("new_data")
    @Expose
    private NewData newData;
    @SerializedName("old_data")
    @Expose
    private OldData oldData;

    public NewData getNewData() {
        return newData;
    }

    public void setNewData(NewData newData) {
        this.newData = newData;
    }

    public OldData getOldData() {
        return oldData;
    }

    public void setOldData(OldData oldData) {
        this.oldData = oldData;
    }
    public static class NewData {

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
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("isEditable")
        @Expose
        private String isEditable;
        @SerializedName("replace_tag")
        @Expose
        private String replaceTag;
        @SerializedName("submitted_date")
        @Expose
        private String submittedDate;

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

        public String getIsEditable() {
            return isEditable;
        }

        public void setIsEditable(String isEditable) {
            this.isEditable = isEditable;
        }

        public String getReplaceTag() {
            return replaceTag;
        }

        public void setReplaceTag(String replaceTag) {
            this.replaceTag = replaceTag;
        }

        public String getSubmittedDate() {
            return submittedDate;
        }

        public void setSubmittedDate(String submittedDate) {
            this.submittedDate = submittedDate;
        }

    }
    public static class OldData {

        @SerializedName("trxid")
        @Expose
        private String trxid;
        @SerializedName("reference_no")
        @Expose
        private String referenceNo;
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("amount")
        @Expose
        private String amount;

        public String getTrxid() {
            return trxid;
        }

        public void setTrxid(String trxid) {
            this.trxid = trxid;
        }

        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
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

    }
}
