package ext.android.arch.lifecycle.rxjava2;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import ext.android.arch.lifecycle.rxjava2.Functions;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class LifecycleTransformer<T> implements ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {

    private final Observable<?> mObservable;

    public LifecycleTransformer(@NonNull Observable<?> observable) {
        this.mObservable = observable;
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return Completable.ambArray(upstream, mObservable.flatMapCompletable(Functions.CANCEL_COMPLETABLE));
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.takeUntil(mObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.takeUntil(mObservable.firstElement());
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.takeUntil(mObservable);
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.takeUntil(mObservable.firstOrError());
    }
}
