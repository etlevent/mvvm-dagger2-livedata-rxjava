package ext.arch.components.lifecycle.reactivex;


import android.arch.lifecycle.LiveData;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

final class RxJavaLiveDataPlugins {
    private RxJavaLiveDataPlugins() {
        throw new AssertionError("no instance.");
    }

    static <T> Observable<T> toObservable(LiveData<T> source) {
        return new LiveDataObservable<>(source);
    }

    static <T> Observable<T> toObservable(LiveData<T> source, T defValue) {
        return new LiveDataObservable<>(source, defValue);
    }

    static <T> Flowable<T> toFlowable(LiveData<T> source) {
        return new LiveDataObservable<>(source).toFlowable(BackpressureStrategy.LATEST);
    }

    static <T> Flowable<T> toFlowable(LiveData<T> source, T defValue) {
        return new LiveDataObservable<>(source, defValue).toFlowable(BackpressureStrategy.LATEST);
    }

    static <T> Single<T> toSingle(LiveData<T> source) {
        return new LiveDataObservable<>(source).firstOrError();
    }

    static <T> Single<T> toSingle(LiveData<T> source, T defValue) {
        return new LiveDataObservable<>(source, defValue).firstOrError();
    }

    static <T> Maybe<T> toMaybe(LiveData<T> source) {
        return new LiveDataObservable<>(source).firstElement();
    }

    static <T> Maybe<T> toMaybe(LiveData<T> source, T defValue) {
        return new LiveDataObservable<>(source, defValue).firstElement();
    }

    static <T> Completable toCompletable(LiveData<T> source) {
        return new LiveDataCompletable<>(source);
    }

    static <T> Completable toCompletable(LiveData<T> source, T defValue) {
        return new LiveDataCompletable<>(source, defValue);
    }
}
