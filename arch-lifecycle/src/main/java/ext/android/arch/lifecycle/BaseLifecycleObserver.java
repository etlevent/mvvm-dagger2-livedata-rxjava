package ext.android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public abstract class BaseLifecycleObserver implements ILifecycleObserver {

    private Lifecycle mLifecycle;

    public BaseLifecycleObserver(@NonNull Lifecycle lifecycle) {
        this.mLifecycle = checkNotNull(lifecycle, "lifecycle == null");
        this.mLifecycle.addObserver(this);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @CallSuper
    @Override
    public void onDestroy() {
        this.mLifecycle.removeObserver(this);
    }

    public static @NonNull
    <T> T checkNotNull(final T reference, final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
}
