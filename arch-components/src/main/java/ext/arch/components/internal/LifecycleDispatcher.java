package ext.arch.components.internal;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import ext.arch.components.api.ActivityStack;
import ext.arch.components.annotations.NonStack;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
final class LifecycleDispatcher {

    private static AtomicBoolean sInitialized = new AtomicBoolean(false);

    public static void init(@NonNull Context context) {
        if (sInitialized.getAndSet(true)) {
            return;
        }
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new DispatcherActivityCallback());
    }

    static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            final Class<? extends Activity> cls = activity.getClass();
            if (!cls.isAnnotationPresent(NonStack.class)) {
                Log.i("Stack", "push Activity: " + cls.getSimpleName());
                ActivityStack.get().push(activity);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            final Class<? extends Activity> cls = activity.getClass();
            if (!cls.isAnnotationPresent(NonStack.class)) {
                Log.i("Stack", "pop Activity: " + cls.getSimpleName());
                ActivityStack.get().pop(activity);
            }
        }
    }
}
