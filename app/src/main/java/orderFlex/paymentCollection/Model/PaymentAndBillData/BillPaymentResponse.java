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
    @SerializedName("message")
    @Expose
    private String message;

    public boolean isSuccessfull() {
        return isSuccessfull;
    }

    public void setSuccessfull(boolean successfull) {
        isSuccessfull = successfull;
    }

    @SerializedName("isSuccessfull")
    @Expose
    private boolean isSuccessfull;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
