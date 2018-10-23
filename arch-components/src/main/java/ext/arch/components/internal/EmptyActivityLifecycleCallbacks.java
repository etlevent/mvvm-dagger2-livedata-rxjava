package ext.arch.components.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class EmptyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLifecycle";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "[onActivityCreated] " + activity + " [savedInstanceState] " + savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "[onActivityStarted]" + activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "[onActivityResumed]" + activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.w(TAG, "[onActivityPaused]" + activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.w(TAG, "[onActivityStopped]" + activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.w(TAG, "[onActivitySaveInstanceState]" + activity + " [outState] " + outState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "[onActivityDestroyed]" + activity);
    }
}
