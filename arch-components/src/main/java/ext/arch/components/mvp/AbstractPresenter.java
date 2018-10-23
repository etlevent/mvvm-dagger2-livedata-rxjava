package ext.arch.components.mvp;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import ext.android.arch.lifecycle.rxjava2.RxLifecycleObserver;

public abstract class AbstractPresenter<V extends BaseView, P extends BasePresenter> extends RxLifecycleObserver {
    protected final V mView;

    public AbstractPresenter(@NonNull V view, @NonNull Lifecycle lifecycle) {
        super(lifecycle);
        this.mView = view;
        this.mView.setPresenter(getPresenter());
    }

    @NonNull
    protected abstract P getPresenter();
}
