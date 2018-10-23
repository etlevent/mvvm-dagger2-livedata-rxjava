package ext.arch.components.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ext.arch.components.R;
import io.reactivex.disposables.Disposable;

/**
 * Created by roothost on 2018/1/5.
 */

public abstract class ProgressObserver<T> extends BaseObserver<T> {
    private WeakReference<Activity> mActivityRef;
    private Context mContext;
    private Dialog mProgressDialog;

    public ProgressObserver(@NonNull Activity activity) {
        mContext = activity.getApplicationContext();
        mActivityRef = new WeakReference<>(activity);
    }

    public ProgressObserver(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    @NonNull
    protected abstract String getMessage();

    @CallSuper
    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (isActivityAvailable() && !TextUtils.isEmpty(getMessage())) {
            if (mProgressDialog == null) {
                View view = LayoutInflater.from(mActivityRef.get()).inflate(R.layout.arch_layout_progress_dialog, null);
                TextView messageView = view.findViewById(R.id.message);
                messageView.setText(getMessage());
                mProgressDialog = new AlertDialog.Builder(mActivityRef.get())
                        .setView(view)
                        .setCancelable(false)
                        .create();
            }
            showDismissDialog(true);
        }
    }

    @CallSuper
    @Override
    public void onNext(T t) {
        super.onNext(t);
        showDismissDialog(false);
        Log.i("OkHttp", "onNext=" + t);
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        super.onError(e);
        showDismissDialog(false);
        Log.e("OkHttp", "onError", e);
    }

    @CallSuper
    @Override
    public void onComplete() {
        super.onComplete();
        showDismissDialog(false);
    }

    private boolean isActivityAvailable() {
        return mActivityRef != null && mActivityRef.get() != null
                && !mActivityRef.get().isFinishing()
                && !mActivityRef.get().isDestroyed();
    }

    private void showDismissDialog(boolean show) {
        if (isActivityAvailable() && mProgressDialog != null) {
            if (show && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            } else if (!show && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    protected Context getContext() {
        return mContext;
    }

    protected Activity getActivity() {
        if (mActivityRef != null) {
            return mActivityRef.get();
        }
        return null;
    }

    protected void showToast(@StringRes int resId) {
        if (getContext() != null) {
            Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
        } else {
            throw new IllegalArgumentException("Can not show Toast with Null Context.");
        }
    }

    protected void showToast(@NonNull CharSequence charSequence) {
        if (getContext() != null) {
            Toast.makeText(getContext(), charSequence, Toast.LENGTH_SHORT).show();
        } else {
            throw new IllegalArgumentException("Can not show Toast with Null Context.");
        }
    }
}
