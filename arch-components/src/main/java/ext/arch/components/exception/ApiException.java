package ext.arch.components.exception;

public class ApiException extends RuntimeException {
    private int code;
    private Object obj;

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(int code, String message, Object obj) {
        super(message);
        this.code = code;
        this.obj = obj;
    }

    public ApiException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApiException(int code, String message, Object obj, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public Object getObj() {
        return obj;
    }
}
