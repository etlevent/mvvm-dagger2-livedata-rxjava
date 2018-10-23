package ext.android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

public interface ILifecycleObserver extends LifecycleObserver {
    /**
     * Fragment or Activity Lifecycle: onCreate
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate();

    /**
     * Fragment or Activity Lifecycle: onStart
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart();

    /**
     * Fragment or Activity Lifecycle: onResume
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume();

    /**
     * Fragment or Activity Lifecycle: onPause
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause();

    /**
     * Fragment or Activity Lifecycle: onStop
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop();

    /**
     * Fragment or Activity Lifecycle: onDestroy
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy();

    /**
     * will be called when lifecycle changed.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onAnyLifecycle(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event);
}
