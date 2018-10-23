package ext.arch.components.network;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by roothost on 2018/1/4.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException
                || e instanceof ConnectException) {
            //todo: fixme
        }
    }

    @Override
    public void onComplete() {

    }
}
