package orderFlex.paymentCollection.Model.LoginData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginClientRequestBody {

    @SerializedName("android_id")
    @Expose
    private String androidId;

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
}
