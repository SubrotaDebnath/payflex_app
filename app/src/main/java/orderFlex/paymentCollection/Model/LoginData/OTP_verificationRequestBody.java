package orderFlex.paymentCollection.Model.LoginData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTP_verificationRequestBody {
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("android_id")
    @Expose
    private String androidId;
    @SerializedName("otp")
    @Expose
    private String otp;

    public OTP_verificationRequestBody(String clientId, String androidId, String otp) {
        this.clientId = clientId;
        this.androidId = androidId;
        this.otp = otp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
