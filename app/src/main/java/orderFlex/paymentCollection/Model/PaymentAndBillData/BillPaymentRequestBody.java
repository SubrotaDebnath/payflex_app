package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillPaymentRequestBody {

    @SerializedName("trxid")
    @Expose
    private String trxid;
    @SerializedName("payment_mode_id")
    @Expose
    private String paymentModeId;
    @SerializedName("financial_institution_id")
    @Expose
    private String financial_institution_id;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getFinancial_institution_id() {
        return financial_institution_id;
    }

    public void setFinancial_institution_id(String financial_institution_id) {
        this.financial_institution_id = financial_institution_id;
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
}
