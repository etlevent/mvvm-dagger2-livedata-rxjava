package ext.arch.components.internal;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo(RestrictTo.Scope.LIBRARY)
final class AppMemoryRecycled {

    private static final String TAG = "AppMemoryRecycled";

    private static volatile AppMemoryRecycled _instance;
    private static final AtomicBoolean sInitialized = new AtomicBoolean(false);
    private final AtomicBoolean isRecycled;
    private String mLaunchActivityName;

    @NonNull
    private Application mApplication;

    public static AppMemoryRecycled get() {
        if (_instance == null) {
            synchronized (AppMemoryRecycled.class) {
                if (_instance == null) {
                    _instance = new AppMemoryRecycled();
                }
            }
        }
        return _instance;
    }

    private AppMemoryRecycled() {
        isRecycled = new AtomicBoolean(true);
    }

    public void init(@NonNull Context context) {
        if (sInitialized.getAndSet(true)) {
            return;
        }
        mApplication = (Application) context.getApplicationContext();
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        mLaunchActivityName = getLaunchActivityName();
    }

    public boolean isMemoryRecycled() {
        return isRecycled.get();
    }

    @CheckResult
    @Nullable
    private String getLaunchActivityName() {
        checkInitialized();
        final Intent intent = mApplication.getPackageManager().getLaunchIntentForPackage(mApplication.getPackageName());
        if (intent != null) {
            final ComponentName componentName = intent.getComponent();
            if (componentName != null) {
                return componentName.getClassName();
            }
        }
        return null;
    }

    private static void checkInitialized() {
        if (!sInitialized.get()) {
            throw new RuntimeException("AppMemoryRecycled must call init() first.");
        }
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new EmptyActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (mLaunchActivityName == null) {
                return;
            }
            final ComponentName componentName = activity.getComponentName();
            if (mLaunchActivityName.equals(componentName.getClassName())) {
                Log.i(TAG, "Now Application Launch Normal. " + activity);
                isRecycled.getAndSet(false);
            } else {
                if (isRecycled.get()) {
                    Log.i(TAG, "Now Application Memory is Recycled in [" + activity + "] . Launch Application [" + mLaunchActivityName + "]");
                    final Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(launchIntent);
                    activity.finish();
                } else {
                    Log.i(TAG, "Normal Create. " + activity);
                }
            }
        }
    };
}
