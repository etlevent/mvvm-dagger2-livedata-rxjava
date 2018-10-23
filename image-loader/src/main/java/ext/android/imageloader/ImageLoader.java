package ext.android.imageloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by ROOT on 2017/7/27.
 */

public interface ImageLoader {
    void showImage(@NonNull View view, @NonNull Object model, @Nullable ImageLoaderOptions options);

    <T> void loadOnly(@NonNull Context context, @NonNull Object model, @Nullable ImageLoaderOptions options, @Nullable IRequestListener<T> listener);

    void resume(@NonNull Context context);

    void pause(@NonNull Context context);
}
