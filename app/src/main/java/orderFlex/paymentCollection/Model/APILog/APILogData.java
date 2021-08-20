package orderFlex.paymentCollection.Model.APILog;

public class APILogData {
    private int id;
    private String callName;
    private String callURL;
    private String callTime;
    private String requestBody;
    private String responseCode;
    private String responseBody;
    private String exception;
    private String responseTime;

    public APILogData() {
    }

    public APILogData(int id, String callName, String callURL, String callTime, String requestBody, String responseCode, String responseBody, String exception, String responseTime) {
        this.id = id;
        this.callName = callName;
        this.callURL = callURL;
        this.callTime = callTime;
        this.requestBody = requestBody;
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.exception = exception;
        this.responseTime = responseTime;
    }

    public APILogData(String callName, String callURL, String callTime, String requestBody, String responseCode, String responseBody, String exception, String responseTime) {
        this.callName = callName;
        this.callURL = callURL;
        this.callTime = callTime;
        this.requestBody = requestBody;
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.exception = exception;
        this.responseTime = responseTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallURL() {
        return callURL;
    }

    public void setCallURL(String callURL) {
        this.callURL = callURL;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
