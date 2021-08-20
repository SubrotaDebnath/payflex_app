package orderFlex.paymentCollection.Model.SaveOrderData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantListResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("plant_List")
    @Expose
    private List<PlantList> plantList = null;

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

    public List<PlantList> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<PlantList> plantList) {
        this.plantList = plantList;
    }
    public class PlantList {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("plant")
        @Expose
        private String plant;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlant() {
            return plant;
        }

        public void setPlant(String plant) {
            this.plant = plant;
        }

    }
}
