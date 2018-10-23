package ext.arch.components.base;

import android.arch.lifecycle.Lifecycle;
import android.support.v4.app.Fragment;

import ext.android.arch.lifecycle.rxjava2.IRxLifecycleBinding;
import ext.arch.components.util.Preconditions;

/**
 * @author roothost
 * @date 2018/1/11
 */

public abstract class BaseFragment extends Fragment {
    protected IRxLifecycleBinding<Lifecycle.Event> mLifecycleBinding;

    protected void setLifecycleBinding(IRxLifecycleBinding<Lifecycle.Event> lifecycleBinding) {
        Preconditions.checkNotNull(lifecycleBinding, "lifecycle binding Never be Null");
        this.mLifecycleBinding = lifecycleBinding;
    }
}
