package ext.android.arch.lifecycle.rxjava2;

import android.support.annotation.RestrictTo;

import java.util.concurrent.CancellationException;

import io.reactivex.Completable;
import io.reactivex.functions.Function;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class Functions {
    private Functions() {
        throw new AssertionError("no instance.");
    }

    static final Function<Object, Completable> CANCEL_COMPLETABLE = o -> Completable.error(new CancellationException());
}
