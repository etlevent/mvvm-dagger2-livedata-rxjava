package ext.android.arch.lifecycle.rxjava2;

import io.reactivex.Observable;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;

public interface IRxLifecycleBinding<E extends Enum> {
    @NonNull
    @CheckReturnValue
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Observable<E> lifecycle,
                                               @NonNull E event);

    @NonNull
    @CheckReturnValue
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull E event);

    @NonNull
    @CheckReturnValue
    Observable<E> lifecycle();
}
