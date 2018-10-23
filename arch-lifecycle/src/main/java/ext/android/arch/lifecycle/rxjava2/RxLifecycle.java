package ext.android.arch.lifecycle.rxjava2;


import android.support.annotation.RestrictTo;

import io.reactivex.Observable;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class RxLifecycle {

    private RxLifecycle() {
        throw new AssertionError("no instance.");
    }

    @NonNull
    @CheckReturnValue
    public static <T, E extends Enum> LifecycleTransformer<T> bindUntilEvent(@NonNull Observable<E> lifecycle,
                                                                             @NonNull E event) {
        return bind(takeUntilEvent(lifecycle, event));
    }

    @NonNull
    @CheckReturnValue
    public static <E extends Enum> Observable<E> takeUntilEvent(@NonNull Observable<E> lifecycle,
                                                                @NonNull E event) {
        return lifecycle.filter(e -> event.equals(e));
    }

    @NonNull
    @CheckReturnValue
    public static <T, E extends Enum> LifecycleTransformer<T> bind(@NonNull Observable<E> lifecycle) {
        return new LifecycleTransformer<>(lifecycle);
    }

}
