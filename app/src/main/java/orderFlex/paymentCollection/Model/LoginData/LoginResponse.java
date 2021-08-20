package orderFlex.paymentCollection.Model.LoginData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("catagory_id")
    @Expose
    private String catagoryId;
    @SerializedName("client_code")
    @Expose
    private String clientCode;
    @SerializedName("virtual_account_no")
    @Expose
    private String virtualAccountNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("representative_name")
    @Expose
    private String representativeName;
    @SerializedName("office_id")
    @Expose
    private String officeId;
    @SerializedName("client_parent_id")
    @Expose
    private String clientParentId;
    @SerializedName("handler_id")
    @Expose
    private String handlerId;
    @SerializedName("client_pairID")
    @Expose
    private String clientPairID;
    @SerializedName("created_date_time")
    @Expose
    private Object createdDateTime;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("is_active")
    @Expose
    private Object isActive;
    @SerializedName("image_url")
    @Expose
    private String image_url;
    @SerializedName("contacts")
    @Expose
    private List<Contact> contacts = null;
    @SerializedName("isNewDevice")
    @Expose
    private Boolean isNewDevice;
    @SerializedName("isValidDevice")
    @Expose
    private Boolean isValidDevice;

    public String getImage_url() {
        return image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getVirtualAccountNo() {
        return virtualAccountNo;
    }

    public void setVirtualAccountNo(String virtualAccountNo) {
        this.virtualAccountNo = virtualAccountNo;
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(String catagoryId) {
        this.catagoryId = catagoryId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getClientParentId() {
        return clientParentId;
    }

    public void setClientParentId(String clientParentId) {
        this.clientParentId = clientParentId;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getClientPairID() {
        return clientPairID;
    }

    public void setClientPairID(String clientPairID) {
        this.clientPairID = clientPairID;
    }

    public Object getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Object createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getIsActive() {
        return isActive;
    }

    public void setIsActive(Object isActive) {
        this.isActive = isActive;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Boolean getNewDevice() {
        return isNewDevice;
    }

    public void setNewDevice(Boolean newDevice) {
        isNewDevice = newDevice;
    }

    public Boolean getValidDevice() {
        return isValidDevice;
    }

    public void setValidDevice(Boolean validDevice) {
        isValidDevice = validDevice;
    }

    public class Contact {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("contact_value")
        @Expose
        private String contactValue;
        @SerializedName("contact_type_id")
        @Expose
        private String contactTypeId;
        @SerializedName("contact_type")
        @Expose
        private String contactType;
        @SerializedName("parent_id")
        @Expose
        private String parentId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContactValue() {
            return contactValue;
        }

        public void setContactValue(String contactValue) {
            this.contactValue = contactValue;
        }

        public String getContactTypeId() {
            return contactTypeId;
        }

        public void setContactTypeId(String contactTypeId) {
            this.contactTypeId = contactTypeId;
        }

        public String getContactType() {
            return contactType;
        }

        public void setContactType(String contactType) {
            this.contactType = contactType;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

    }
}
