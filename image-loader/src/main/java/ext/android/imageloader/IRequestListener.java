package ext.android.imageloader;

import android.support.annotation.Nullable;

/**
 * Created by roothost on 2018/3/19.
 */

public interface IRequestListener<R> {
    boolean onLoadFailed(@Nullable Exception e, Object model);

    boolean onResourceReady(R resource, Object model);
}
