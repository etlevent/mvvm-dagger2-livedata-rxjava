package ext.arch.components.model;

/**
 * Created by roothost on 2018/1/5.
 */

public class HTTPHeader {
    /**
     * syb_operatorCode
     */
    private String operatorCode;
    /**
     * syb_sessionToken
     */
    private String sessionToken;
    /**
     * syb_consumerSeqNo
     */
    private String consumerSeqNo;
    /**
     * syb_appId
     */
    private String appId;
    /**
     * syb_appKey
     */
    private String appKey;

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getConsumerSeqNo() {
        return consumerSeqNo;
    }

    public void setConsumerSeqNo(String consumerSeqNo) {
        this.consumerSeqNo = consumerSeqNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "HTTPHeader{" +
                "operatorCode='" + operatorCode + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", appId='" + appId + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
