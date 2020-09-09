package orderFlex.paymentCollection.Model.PaymentAndBillData;

public class PaymentQueueRequestData {
    private int id;
    private String filename;
    private String fileType;
    private String clientID;
    private String fileDetail;
    private String extension;
    private String order_code;
    private String payment_id;
    private String sourcePath;
    private String isSync;

    public PaymentQueueRequestData(String filename, String fileType, String clientID, String fileDetail, String extension, String order_code, String payment_id, String sourcePath,String isSync) {
        this.filename = filename;
        this.fileType = fileType;
        this.clientID = clientID;
        this.fileDetail = fileDetail;
        this.extension = extension;
        this.order_code = order_code;
        this.payment_id = payment_id;
        this.sourcePath = sourcePath;
        this.isSync=isSync;
    }

    public PaymentQueueRequestData(int id, String filename, String fileType, String clientID, String fileDetail, String extension, String order_code, String payment_id, String sourcePath, String isSync) {
        this.id = id;
        this.filename = filename;
        this.fileType = fileType;
        this.clientID = clientID;
        this.fileDetail = fileDetail;
        this.extension = extension;
        this.order_code = order_code;
        this.payment_id = payment_id;
        this.sourcePath = sourcePath;
        this.isSync = isSync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getFileDetail() {
        return fileDetail;
    }

    public void setFileDetail(String fileDetail) {
        this.fileDetail = fileDetail;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
