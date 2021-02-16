package orderFlex.paymentCollection.Model.AppSetup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSetupResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("isUpdatedApp")
        @Expose
        private Boolean isUpdatedApp;
        @SerializedName("isSystemUnderMaintenance")
        @Expose
        private Boolean isSystemUnderMaintenance;
        @SerializedName("lastVersionOfApp")
        @Expose
        private String lastVersionOfApp;
        @SerializedName("updatedAppLink")
        @Expose
        private String updatedAppLink;
        @SerializedName("isMessageForUser")
        @Expose
        private Boolean isMessageForUser;
        @SerializedName("customWebViewURL")
        @Expose
        private String customWebViewURL;
        @SerializedName("isOffer")
        @Expose
        private Boolean isOffer;
        @SerializedName("debugMode")
        @Expose
        private Boolean debugMode;
        @SerializedName("goWithCheck")
        @Expose
        private Boolean goWithCheck;

        public Boolean getSystemUnderMaintenance() {
            return isSystemUnderMaintenance;
        }

        public Boolean getMessageForUser() {
            return isMessageForUser;
        }

        public Boolean getGoWithCheck() {
            return goWithCheck;
        }

        public Boolean getUpdatedApp() {
            return isUpdatedApp;
        }

        public Boolean getOffer() {
            return isOffer;
        }

        public void setOffer(Boolean offer) {
            isOffer = offer;
        }

        public Boolean getDebugMode() {
            return debugMode;
        }

        public void setDebugMode(Boolean debugMode) {
            this.debugMode = debugMode;
        }

        public Boolean getIsUpdatedApp() {
            return isUpdatedApp;
        }

        public void setIsUpdatedApp(Boolean isUpdatedApp) {
            this.isUpdatedApp = isUpdatedApp;
        }

        public Boolean getIsSystemUnderMaintenance() {
            return isSystemUnderMaintenance;
        }

        public void setIsSystemUnderMaintenance(Boolean isSystemUnderMaintenance) {
            this.isSystemUnderMaintenance = isSystemUnderMaintenance;
        }

        public String getLastVersionOfApp() {
            return lastVersionOfApp;
        }

        public void setLastVersionOfApp(String lastVersionOfApp) {
            this.lastVersionOfApp = lastVersionOfApp;
        }

        public String getUpdatedAppLink() {
            return updatedAppLink;
        }

        public void setUpdatedAppLink(String updatedAppLink) {
            this.updatedAppLink = updatedAppLink;
        }

        public Boolean getIsMessageForUser() {
            return isMessageForUser;
        }

        public void setIsMessageForUser(Boolean isMessageForUser) {
            this.isMessageForUser = isMessageForUser;
        }

        public String getCustomWebViewURL() {
            return customWebViewURL;
        }

        public void setCustomWebViewURL(String customWebViewURL) {
            this.customWebViewURL = customWebViewURL;
        }

    }
}
