package orderFlex.paymentCollection.Model.OffersListDataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OffersListPojo {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("offer_title")
        @Expose
        private String offerTitle;
        @SerializedName("offer_discription")
        @Expose
        private String offerDiscription;
        @SerializedName("offer_image_id")
        @Expose
        private String offerImageId;
        @SerializedName("offer_start_date")
        @Expose
        private String offerStartDate;
        @SerializedName("offer_end_time")
        @Expose
        private String offerEndTime;
        @SerializedName("offer_code")
        @Expose
        private String offerCode;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("image_name")
        @Expose
        private String imageName;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOfferTitle() {
            return offerTitle;
        }

        public void setOfferTitle(String offerTitle) {
            this.offerTitle = offerTitle;
        }

        public String getOfferDiscription() {
            return offerDiscription;
        }

        public void setOfferDiscription(String offerDiscription) {
            this.offerDiscription = offerDiscription;
        }

        public String getOfferImageId() {
            return offerImageId;
        }

        public void setOfferImageId(String offerImageId) {
            this.offerImageId = offerImageId;
        }

        public String getOfferStartDate() {
            return offerStartDate;
        }

        public void setOfferStartDate(String offerStartDate) {
            this.offerStartDate = offerStartDate;
        }

        public String getOfferEndTime() {
            return offerEndTime;
        }

        public void setOfferEndTime(String offerEndTime) {
            this.offerEndTime = offerEndTime;
        }

        public String getOfferCode() {
            return offerCode;
        }

        public void setOfferCode(String offerCode) {
            this.offerCode = offerCode;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }
}
