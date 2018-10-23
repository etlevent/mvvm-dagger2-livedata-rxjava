package ext.arch.components.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ext.arch.components.annotations.HTTPStatus;
import ext.arch.components.exception.ApiException;


public class HTTPResponse<T> implements Response<T> {

    @Expose
    @HTTPStatus
    @SerializedName("syb_status")
    private int status;

    @Expose
    @SerializedName("syb_info")
    private String info;

    @Expose
    @SerializedName("syb_providerSeqNo")
    private String providerSeqNo;

    @Expose
    @SerializedName("syb_consumerSeqNo")
    private String consumerSeqNo;

    @Expose
    @SerializedName("syb_data")
    private T data;

    @Expose
    @SerializedName("syb_sessionToken")
    private String sessionToken;

    public HTTPResponse(String consumerSeqNo, String info, String providerSeqNo, int status, T data) {
        this.consumerSeqNo = consumerSeqNo;
        this.info = info;
        this.providerSeqNo = providerSeqNo;
        this.status = status;
        this.data = data;
    }

    @Override
    public T body() {
        return this.data;
    }

    public void body(T body) {
        this.data = body;
    }

    @Override
    public boolean isSuccess() {
        return this.status == HTTPStatus.SUCCESS;
    }

    @Override
    public RuntimeException error() {
        return new ApiException(this.status, this.info, this.data);
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getProviderSeqNo() {
        return providerSeqNo;
    }

    public String getConsumerSeqNo() {
        return consumerSeqNo;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    @Override
    public String toString() {
        return "HTTPResponse{" +
                "status='" + status + '\'' +
                ",\n info='" + info + '\'' +
                ",\n providerSeqNo='" + providerSeqNo + '\'' +
                ",\n consumerSeqNo='" + consumerSeqNo + '\'' +
                ",\n data=" + data +
                ",\n sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
