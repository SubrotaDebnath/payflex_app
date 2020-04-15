package orderFlex.paymentCollection.Model.PaymentAndBillData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillPaymentResponse {
    @SerializedName("trxid")
    @Expose
    private String trxid;
    @SerializedName("inserted_code")
    @Expose
    private String inserted_code;

    public String getTrxid() {
        return trxid;
    }

    public void setTrxid(String trxid) {
        this.trxid = trxid;
    }

    public String getInserted_code() {
        return inserted_code;
    }

    public void setInserted_code(String inserted_code) {
        this.inserted_code = inserted_code;
    }
}
