package orderFlex.paymentCollection.Model.TodayOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerOrderListResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("messege")
    @Expose
    private String message;
    @SerializedName("order_list")
    @Expose
    private List<Order_list> orderDetails = null;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Order_list> getOrderDetails() {
        return orderDetails;
    }

    public class Order_list {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("taking_date")
        @Expose
        private String takingDate;
        @SerializedName("delivery_date")
        @Expose
        private String deliveryDate;
        @SerializedName("insert_date_time")
        @Expose
        private String insertDateTime;
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("taker_id")
        @Expose
        private String takerId;
        @SerializedName("order_for_client_id")
        @Expose
        private String order_for_client_id;
        @SerializedName("payment_status")
        @Expose
        private String payment_status;
        @SerializedName("total_costs")
        @Expose
        private String total_costs;
        @SerializedName("indent_flag")
        @Expose
        private int indent_flag;
        @SerializedName("isEditable")
        @Expose
        private int isEditable;
        @SerializedName("isSubmitted")
        @Expose
        private int isSubmitted;

        public Order_list(String id, String takingDate, String deliveryDate, String insertDateTime, String orderCode, String takerId, String order_for_client_id, String payment_status, String total_costs, int indent_flag, int isEditable, int isSubmitted) {
            this.id = id;
            this.takingDate = takingDate;
            this.deliveryDate = deliveryDate;
            this.insertDateTime = insertDateTime;
            this.orderCode = orderCode;
            this.takerId = takerId;
            this.order_for_client_id = order_for_client_id;
            this.payment_status = payment_status;
            this.total_costs = total_costs;
            this.indent_flag = indent_flag;
            this.isEditable = isEditable;
            this.isSubmitted = isSubmitted;
        }

        public int getIndent_flag() {
            return indent_flag;
        }

        public int getIsEditable() {
            return isEditable;
        }

        public int getIsSubmitted() {
            return isSubmitted;
        }

        public int isIndent_flag() {
            return indent_flag;
        }

        public String getId() {
            return id;
        }

        public String getTakingDate() {
            return takingDate;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public String getInsertDateTime() {
            return insertDateTime;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public String getTakerId() {
            return takerId;
        }

        public String getOrder_for_client_id() {
            return order_for_client_id;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public String getTotal_costs() {
            return total_costs;
        }
    }
}
