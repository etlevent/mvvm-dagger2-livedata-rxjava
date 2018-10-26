package ext.arch.components.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

public interface FullLifecycleObserver extends LifecycleObserver {
    /**
     * Fragment or Activity Lifecycle: onCreate
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(@NonNull LifecycleOwner owner);

    /**
     * Fragment or Activity Lifecycle: onStart
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart(@NonNull LifecycleOwner owner);

    /**
     * Fragment or Activity Lifecycle: onResume
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume(@NonNull LifecycleOwner owner);

    /**
     * Fragment or Activity Lifecycle: onPause
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(@NonNull LifecycleOwner owner);

    /**
     * Fragment or Activity Lifecycle: onStop
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop(@NonNull LifecycleOwner owner);

    /**
     * Fragment or Activity Lifecycle: onDestroy
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(@NonNull LifecycleOwner owner);

    /**
     * will be called when lifecycle changed.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onAnyLifecycle(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event);
}
