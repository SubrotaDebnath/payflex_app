package orderFlex.paymentCollection.Model.OffersListDataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferResponsePostBody {

    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("seen_datetime")
    @Expose
    private String seenDatetime;
    @SerializedName("is_accepted")
    @Expose
    private String isAccepted;

    public OfferResponsePostBody(String offerId, String clientId, String seenDatetime, String isAccepted) {
        this.offerId = offerId;
        this.clientId = clientId;
        this.seenDatetime = seenDatetime;
        this.isAccepted = isAccepted;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSeenDatetime() {
        return seenDatetime;
    }

    public void setSeenDatetime(String seenDatetime) {
        this.seenDatetime = seenDatetime;
    }

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }
}
