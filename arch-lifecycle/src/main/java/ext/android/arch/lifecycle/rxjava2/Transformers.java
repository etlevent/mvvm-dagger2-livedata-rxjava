package ext.android.arch.lifecycle.rxjava2;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ROOT on 2017/8/15.
 */

public final class Transformers {
    private Transformers() {
        throw new AssertionError("no instance");
    }

    public static <T> ObservableTransformer<T, T> mainio() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> delay(final long delay) {
        return upstream -> upstream.delay(delay, TimeUnit.MILLISECONDS);
    }

    public static <T> SingleTransformer<T, T> singleio() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
