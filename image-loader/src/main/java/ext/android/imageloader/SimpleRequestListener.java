package ext.android.imageloader;

import android.support.annotation.Nullable;

/**
 * Created by roothost on 2018/3/19.
 */

public abstract class SimpleRequestListener<R> implements IRequestListener<R> {
    @Override
    public boolean onLoadFailed(@Nullable Exception e, Object model) {
        return false;
    }
}
