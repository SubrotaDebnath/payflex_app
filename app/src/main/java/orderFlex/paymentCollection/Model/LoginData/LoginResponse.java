package orderFlex.paymentCollection.Model.LoginData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("client_id")
    @Expose
    private String client_id;
    @SerializedName("user_type")
    @Expose
    private String user_type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("presenter_name")
    @Expose
    private String presenter_name;
    @SerializedName("contact_number")
    @Expose
    private String contact_number;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("client_pairID")
    @Expose
    private String client_pairID;
    @SerializedName("client_code")
    @Expose
    private String client_code;
    @SerializedName("created_date_time")
    @Expose
    private String created_date_time;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPresenter_name() {
        return presenter_name;
    }

    public void setPresenter_name(String presenter_name) {
        this.presenter_name = presenter_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient_pairID() {
        return client_pairID;
    }

    public void setClient_pairID(String client_pairID) {
        this.client_pairID = client_pairID;
    }

    public String getClient_code() {
        return client_code;
    }

    public void setClient_code(String client_code) {
        this.client_code = client_code;
    }

    public String getCreated_date_time() {
        return created_date_time;
    }

    public void setCreated_date_time(String created_date_time) {
        this.created_date_time = created_date_time;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    @SerializedName("is_active")
    @Expose
    private Boolean is_active;


}
