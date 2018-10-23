package ext.arch.components.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * Created by roothost on 2018/1/15.
 */

public abstract class LazyFragment extends BaseFragment {
    private static final String TAG = "LazyFragment";
    private boolean isViewCreated;

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBindView(view, savedInstanceState);
        if (!this.isViewCreated && getUserVisibleHint()) {
            onLazyLoad();
            Log.i(TAG, "onViewCreated::onLazyLoad [" + getClass().getSimpleName() + "]");
        }
        this.isViewCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isViewCreated && isPrepared() && isVisibleToUser && getActivity() != null) {
            onLazyLoad();
            Log.i(TAG, "setUserVisibleHint::onLazyLoad [" + getClass().getSimpleName() + "]");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.isViewCreated = false;
    }

    protected abstract void onBindView(@Nullable View view, @Nullable Bundle savedInstanceState);

    protected abstract void onLazyLoad();

    protected boolean isPrepared() {
        return true;
    }
}
