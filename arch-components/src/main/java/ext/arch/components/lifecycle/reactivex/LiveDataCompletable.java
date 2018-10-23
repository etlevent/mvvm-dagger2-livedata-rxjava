package ext.arch.components.lifecycle.reactivex;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.MainThreadDisposable;

class LiveDataCompletable<T> extends Completable {
    private final LiveData<T> source;
    private T valueIfNull;

    LiveDataCompletable(LiveData<T> source) {
        this.source = source;
    }

    LiveDataCompletable(LiveData<T> source, @NonNull T valueIfNull) {
        this.source = source;
        this.valueIfNull = valueIfNull;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        final LiveDataCompletableObserver<T> liveDataObserver = new LiveDataCompletableObserver<>(this.source, observer, this.valueIfNull);
        observer.onSubscribe(liveDataObserver);
        this.source.observeForever(liveDataObserver);
    }

    static class LiveDataCompletableObserver<T> extends MainThreadDisposable implements Observer<T> {
        final LiveData<T> source;
        final CompletableObserver observer;
        final T valueIfNull;

        LiveDataCompletableObserver(LiveData<T> source, CompletableObserver observer, T valueIfNull) {
            this.source = source;
            this.observer = observer;
            this.valueIfNull = valueIfNull;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (isDisposed()) {
                return;
            }
            if (t != null || valueIfNull != null) {
                this.observer.onComplete();
            } else {
                this.observer.onError(new NullPointerException(
                        "convert liveData value t to RxJava onNext(t), t cannot be null"));
            }
        }

        @Override
        protected void onDispose() {
            this.source.removeObserver(this);
        }
    }
}
