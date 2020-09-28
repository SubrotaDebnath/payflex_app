package orderFlex.paymentCollection.Model.AppSetup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSetupRequestBody {
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("android_id")
    @Expose
    private String androidId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("log")
    @Expose
    private String log;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("screen_dimensions")
    @Expose
    private ScreenDimensions screenDimensions;

    public AppSetupRequestBody(String appVersion, String androidId, String lat, String log, String dateTime, ScreenDimensions screenDimensions) {
        this.appVersion = appVersion;
        this.androidId = androidId;
        this.lat = lat;
        this.log = log;
        this.dateTime = dateTime;
        this.screenDimensions = screenDimensions;
    }


    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ScreenDimensions getScreenDimensions() {
        return screenDimensions;
    }

    public void setScreenDimensions(ScreenDimensions screenDimensions) {
        this.screenDimensions = screenDimensions;
    }
    public static class ScreenDimensions {

        @SerializedName("widthPixels")
        @Expose
        private int widthPixels;
        @SerializedName("heightPixels")
        @Expose
        private int heightPixels;
        @SerializedName("densityDpi")
        @Expose
        private int densityDpi;
        @SerializedName("xdpi")
        @Expose
        private float xdpi;
        @SerializedName("ydpi")
        @Expose
        private float ydpi;

        public ScreenDimensions(int widthPixels, int heightPixels, int densityDpi, float xdpi, float ydpi) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
            this.densityDpi = densityDpi;
            this.xdpi = xdpi;
            this.ydpi = ydpi;
        }

        public int getWidthPixels() {
            return widthPixels;
        }

        public void setWidthPixels(int widthPixels) {
            this.widthPixels = widthPixels;
        }

        public int getHeightPixels() {
            return heightPixels;
        }

        public void setHeightPixels(int heightPixels) {
            this.heightPixels = heightPixels;
        }

        public int getDensityDpi() {
            return densityDpi;
        }

        public void setDensityDpi(int densityDpi) {
            this.densityDpi = densityDpi;
        }

        public float getXdpi() {
            return xdpi;
        }

        public void setXdpi(float xdpi) {
            this.xdpi = xdpi;
        }

        public float getYdpi() {
            return ydpi;
        }

        public void setYdpi(float ydpi) {
            this.ydpi = ydpi;
        }
    }
}
