package ext.arch.components.lifecycle.reactivex;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

class LiveDataObservable<T> extends Observable<T> {
    private final LiveData<T> source;
    private T valueIfNull;

    LiveDataObservable(LiveData<T> source) {
        this.source = source;
    }

    LiveDataObservable(LiveData<T> source, @NonNull T valueIfNull) {
        this.source = source;
        this.valueIfNull = valueIfNull;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        final LiveDataObservableObserver<T> liveDataObserver = new LiveDataObservableObserver<>(this.source, observer, this.valueIfNull);
        observer.onSubscribe(liveDataObserver);
        this.source.observeForever(liveDataObserver);
    }


    static class LiveDataObservableObserver<T> extends MainThreadDisposable implements android.arch.lifecycle.Observer<T> {
        final LiveData<T> source;
        final Observer<? super T> observer;
        final T valueIfNull;

        LiveDataObservableObserver(LiveData<T> source, Observer<? super T> observer, T valueIfNull) {
            this.source = source;
            this.observer = observer;
            this.valueIfNull = valueIfNull;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (isDisposed()) {
                return;
            }
            if (t != null) {
                this.observer.onNext(t);
                this.observer.onComplete();
            } else if (this.valueIfNull != null) {
                this.observer.onNext(this.valueIfNull);
                this.observer.onComplete();
            } else {
                this.observer.onError(new NullPointerException(
                        "convert liveData value t to RxJava onNext(t), t cannot be null"));
            }
        }

        @Override
        protected void onDispose() {
            source.removeObserver(this);
        }
    }
}