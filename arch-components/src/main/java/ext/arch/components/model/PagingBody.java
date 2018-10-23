package ext.arch.components.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by roothost on 2018/3/27.
 */

public class PagingBody<T> {
    @Expose
    private T data;
    @Expose
    @SerializedName("total")
    private int total;

    public PagingBody() {
        
    }

    public PagingBody(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PagingBody{" +
                "data=" + data +
                ", total=" + total +
                '}';
    }
}
