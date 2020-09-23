package orderFlex.paymentCollection.Model.UserData;

public class ProfileQueueRequestData {
    private int id;
    private String filename;
    private String fileType;
    private String clientID;
    private String fileDetail;
    private String extension;
    private String sourcePath;
    private String isSync;

    public ProfileQueueRequestData(int id, String filename, String fileType, String clientID, String fileDetail, String extension, String sourcePath, String isSync) {
        this.id = id;
        this.filename = filename;
        this.fileType = fileType;
        this.clientID = clientID;
        this.fileDetail = fileDetail;
        this.extension = extension;
        this.sourcePath = sourcePath;
        this.isSync = isSync;
    }

    public ProfileQueueRequestData(String filename, String fileType, String clientID, String fileDetail, String extension, String sourcePath, String isSync) {
        this.filename = filename;
        this.fileType = fileType;
        this.clientID = clientID;
        this.fileDetail = fileDetail;
        this.extension = extension;
        this.sourcePath = sourcePath;
        this.isSync = isSync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }
}
