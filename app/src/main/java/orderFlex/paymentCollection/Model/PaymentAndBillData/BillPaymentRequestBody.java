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
    @SerializedName("bank_id")
    @Expose
    private String bankId;
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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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
}
