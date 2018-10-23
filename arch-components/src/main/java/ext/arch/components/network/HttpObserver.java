package ext.arch.components.network;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class HttpObserver<T> extends ProgressObserver<T> {

    private String mMessage;

    public HttpObserver(@NonNull Activity activity, @NonNull String message) {
        super(activity);
        this.mMessage = message;
    }

    public HttpObserver(@NonNull Activity activity, @StringRes int id) {
        super(activity);
        this.mMessage = activity.getString(id);
    }

    public HttpObserver(@NonNull Activity activity) {
        super(activity);
    }

    public HttpObserver(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected String getMessage() {
        return mMessage;
    }

    @CallSuper
    @Override
    public final void onError(Throwable e) {
        super.onError(e);
        onError();
        if (e instanceof SocketTimeoutException
                || e instanceof ConnectException) {
            showToast("网络连接超时");
        } else {
            if (!disposeError(e)) {
                showToast(e.getMessage());
            }
        }
    }

    /**
     * dispose with error
     *
     * @param e
     * @return true: stop show default toast; false: continue show toast.
     */
    public boolean disposeError(Throwable e) {
        // do your job with error!
        return false;
    }

    /**
     * all error go to here.
     */
    public void onError() {
        // do with all error!
    }
}
