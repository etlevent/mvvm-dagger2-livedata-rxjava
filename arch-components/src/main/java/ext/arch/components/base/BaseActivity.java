package ext.arch.components.base;

import android.arch.lifecycle.Lifecycle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import ext.android.arch.lifecycle.rxjava2.IRxLifecycleBinding;

/**
 * @author roothost
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected IRxLifecycleBinding<Lifecycle.Event> mLifecycleBinding;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected void setLifecycleBinding(IRxLifecycleBinding<Lifecycle.Event> lifecycleBinding) {
        this.mLifecycleBinding = lifecycleBinding;
    }

}
