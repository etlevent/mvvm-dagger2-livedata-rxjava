package ext.arch.components.api;

import android.app.Activity;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

import jdk8.function.Predicate;


/**
 * trace Activity instance.
 * it will be Ignored if Activity Annotation with {@link ext.arch.components.annotations.NonStack}
 */
public final class ActivityStack {

    private static volatile ActivityStack _instance;

    private Stack<WeakReference<Activity>> mStack;

    public static ActivityStack get() {
        if (_instance == null) {
            synchronized (ActivityStack.class) {
                if (_instance == null) {
                    _instance = new ActivityStack();
                }
            }
        }
        return _instance;
    }

    private ActivityStack() {
        mStack = new Stack<>();
    }

    private boolean hasActivity(@NonNull Activity activity) {
        for (WeakReference<Activity> activityWeakReference : mStack) {
            Activity ref = activityWeakReference.get();
            if (ref != null && ref.equals(activity)) {
                return true;
            }
        }
        return false;
    }

    public void push(@NonNull Activity activity) {
        if (!hasActivity(activity)) {
            mStack.push(new WeakReference<>(activity));
        }
    }

    public void pop(@NonNull Activity activity) {
        for (WeakReference<Activity> activityWeakReference : mStack) {
            Activity ref = activityWeakReference.get();
            if (ref == null) {
                mStack.remove(activityWeakReference);
            } else if (ref.equals(activity)) {
                mStack.remove(activityWeakReference);
                if (isActivityAvailable(ref)) {
                    ref.finish();
                }
                break;
            }
        }
    }

    public void pop(@NonNull Class<? extends Activity> cls) {
        popInternal(cls::equals);
    }

    public void popAllIgnore(@NonNull Class<? extends Activity> cls) {
        popInternal(aClass -> !aClass.equals(cls));
    }

    public void popAll() {
        popInternal(null);
    }

    @CheckResult
    @Nullable
    public Activity top() {
        if (mStack.empty()) {
            return null;
        }
        return mStack.lastElement().get();
    }

    public int size() {
        return mStack.size();
    }

    private void popInternal(@Nullable Predicate<Class<? extends Activity>> filter) {
        Iterator<WeakReference<Activity>> iterator = mStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next().get();
            if (activity == null) {
                iterator.remove();
                continue;
            }
            if (filter == null || filter.test(activity.getClass())) {
                iterator.remove();
                if (isActivityAvailable(activity)) {
                    activity.finish();
                }
            }
        }
    }

    private static boolean isActivityAvailable(Activity activity) {
        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
    }
}
