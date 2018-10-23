package ext.arch.components.lifecycle.reactivex;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ObservableLiveData<T> extends MutableLiveData<T> {

    public ObservableLiveData() {
    }

    public ObservableLiveData(LiveData<T> source) {
        source.observeForever(this::postValue);
    }


    public final Observable<T> toObservable() {
        return RxJavaLiveDataPlugins.toObservable(this);
    }

    public final Observable<T> toObservable(T defValue) {
        return RxJavaLiveDataPlugins.toObservable(this, defValue);
    }

    public final Flowable<T> toFlowable() {
        return RxJavaLiveDataPlugins.toFlowable(this);
    }

    public final Flowable<T> toFlowable(T defValue) {
        return RxJavaLiveDataPlugins.toFlowable(this, defValue);
    }

    public final Single<T> toSingle() {
        return RxJavaLiveDataPlugins.toSingle(this);
    }

    public final Single<T> toSingle(T defValue) {
        return RxJavaLiveDataPlugins.toSingle(this, defValue);
    }

    public final Maybe<T> toMaybe() {
        return RxJavaLiveDataPlugins.toMaybe(this);
    }

    public final Maybe<T> toMaybe(T defValue) {
        return RxJavaLiveDataPlugins.toMaybe(this, defValue);
    }

    public final Completable toCompletable() {
        return RxJavaLiveDataPlugins.toCompletable(this);
    }

    public final Completable toCompletable(T defValue) {
        return RxJavaLiveDataPlugins.toCompletable(this, defValue);
    }
}
