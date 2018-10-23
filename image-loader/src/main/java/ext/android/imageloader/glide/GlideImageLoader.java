package ext.android.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;

import ext.android.imageloader.IRequestListener;
import ext.android.imageloader.ImageLoader;
import ext.android.imageloader.ImageLoaderDispatcher;
import ext.android.imageloader.ImageLoaderOptions;
import ext.android.imageloader.annotations.ResourceType;
import ext.android.imageloader.annotations.ScaleType;

/**
 * Created by ROOT on 2017/7/27.
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void showImage(@NonNull View view, @NonNull Object model, @Nullable ImageLoaderOptions options) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            showImageInternal(imageView, model, options);
        }
    }

    @Override
    public <T> void loadOnly(@NonNull Context context, @NonNull Object model, @Nullable ImageLoaderOptions options, @Nullable IRequestListener<T> listener) {
        loadOnlyInternal(context, model, options, listener);
    }

    @Override
    public void resume(@NonNull Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void pause(@NonNull Context context) {
        Glide.with(context).pauseRequests();
    }

    private void showImageInternal(@NonNull ImageView imageView, Object model, @Nullable ImageLoaderOptions options) {
        RequestBuilder builder = getRequestBuilder(imageView.getContext(), model, options);
        builder.into(imageView);
    }

    private <T> void loadOnlyInternal(@NonNull Context context, @NonNull Object model,
                                      @Nullable ImageLoaderOptions options, @Nullable IRequestListener<T> listener) {
        RequestBuilder builder = getRequestBuilder(context, model, options);
        builder.into(new SimpleTarget<T>() {
            @Override
            public void onResourceReady(@NonNull T resource, @Nullable Transition<? super T> transition) {
                if (listener != null) {
                    listener.onResourceReady(resource, model);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (listener != null) {
                    listener.onLoadFailed(new IllegalArgumentException("load model failed"), model);
                }
            }
        });
    }

    private RequestBuilder getRequestBuilder(@NonNull Context context, @NonNull Object model, @Nullable ImageLoaderOptions options) {
        RequestManager requestManager = Glide.with(context);
        RequestBuilder builder = null;
        if (options != null) {
            builder = createRequestBuilder(requestManager, options.getResourceType());
        }
        if (builder == null) {
            builder = requestManager.load(model);
        } else {
            builder.load(model);
        }
        RequestOptions requestOptions = loadOptions(options);
        if (requestOptions != null) {
            builder.apply(requestOptions);
        }
        if (options != null) {
            if (options.isCrossFade()) {
                builder.transition(new DrawableTransitionOptions().crossFade());
            }
            final boolean isListenProgress = options.getProgressListener() != null;
            if (isListenProgress) {
                ImageLoaderDispatcher.get().addProgressListener(model, options.getProgressListener());
            }

            final boolean isListenRequest = options.getRequestListener() != null;
            if (isListenProgress || isListenRequest) {
                builder.listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        if (isListenProgress) {
                            ImageLoaderDispatcher.get().removeProgressListener(model);
                        }
                        if (isListenRequest) {
                            return options.getRequestListener().onLoadFailed(e, model);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        if (isListenProgress) {
                            ImageLoaderDispatcher.get().removeProgressListener(model);
                        }
                        if (isListenRequest) {
                            return options.getRequestListener().onResourceReady(resource, model);
                        }
                        return false;
                    }
                });
            }
        }
        return builder;
    }

    private RequestOptions loadOptions(ImageLoaderOptions options) {
        if (options == null) return null;
        RequestOptions requestOptions = new RequestOptions();
        if (options.getPlaceholder() != -1) {
            requestOptions.placeholder(options.getPlaceholder());
        }
        if (options.getErrorDrawable() != -1) {
            requestOptions.error(options.getErrorDrawable());
        }
        if (options.getImageSize() != null) {
            final ImageLoaderOptions.ImageSize imageSize = options.getImageSize();
            requestOptions.override(imageSize.getWidth(), imageSize.getHeight());
        }
        if (options.getResourceType() == ResourceType.GIF) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        }
        if (options.isTimeSignature()) {
            requestOptions.signature(new ObjectKey(System.currentTimeMillis()));
        }
        applyScaleType(requestOptions, options.getScaleType());
        return requestOptions;
    }

    private void applyScaleType(RequestOptions requestOptions, @ScaleType int scaleType) {
        switch (scaleType) {
            case ScaleType.FIT_CENTER:
                requestOptions.fitCenter();
                break;
            case ScaleType.CENTER_CROP:
                requestOptions.centerCrop();
                break;
            case ScaleType.CENTER_INSIDE:
                requestOptions.centerInside();
                break;
            default:
                requestOptions.fitCenter();
                break;
        }
    }

    private RequestBuilder createRequestBuilder(RequestManager requestManager, @ResourceType int resourceType) {
        final RequestBuilder requestBuilder;
        switch (resourceType) {
            case ResourceType.DRAWABLE:
                requestBuilder = requestManager.asDrawable();
                break;
            case ResourceType.BITMAP:
                requestBuilder = requestManager.asBitmap();
                break;
            case ResourceType.GIF:
                requestBuilder = requestManager.asGif();
                break;
            default:
                requestBuilder = null;
                break;
        }
        return requestBuilder;
    }
}
