package orderFlex.paymentCollection.Model.UserData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassChangeReqBody {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("re_newPassword")
    @Expose
    private String reNewPassword;

    public PassChangeReqBody(String username, String password, String newPassword, String reNewPassword) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.reNewPassword = reNewPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getReNewPassword() {
        return reNewPassword;
    }

    public void setReNewPassword(String reNewPassword) {
        this.reNewPassword = reNewPassword;
    }
}
