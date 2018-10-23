package ext.android.imageloader;

import android.support.annotation.DrawableRes;
import android.support.annotation.RestrictTo;

import ext.android.imageloader.annotations.ResourceType;
import ext.android.imageloader.annotations.ScaleType;
import ext.android.imageloader.progress.ProgressListener;

/**
 * Created by ROOT on 2017/7/27.
 */

public class ImageLoaderOptions {
    @DrawableRes
    private int placeholder = -1;
    @DrawableRes
    private int errorDrawable = -1;
    private ImageSize imageSize;
    private boolean isCrossFade;
    @ResourceType
    private int resourceType;
    @ScaleType
    private int scaleType;
    private boolean timeSignature;
    private ProgressListener progressListener;
    private IRequestListener requestListener;

    private ImageLoaderOptions(Builder builder) {
        this.placeholder = builder.placeholder;
        this.errorDrawable = builder.errorDrawable;
        this.imageSize = builder.imageSize;
        this.isCrossFade = builder.isCrossFade;
        this.resourceType = builder.resourceType;
        this.scaleType = builder.scaleType;
        this.timeSignature = builder.timeSignature;
        this.progressListener = builder.progressListener;
        this.requestListener = builder.requestListener;
    }

    @DrawableRes
    public int getPlaceholder() {
        return placeholder;
    }

    @DrawableRes
    public int getErrorDrawable() {
        return errorDrawable;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public boolean isCrossFade() {
        return isCrossFade;
    }

    @ResourceType
    public int getResourceType() {
        return resourceType;
    }

    @ScaleType
    public int getScaleType() {
        return scaleType;
    }

    public boolean isTimeSignature() {
        return timeSignature;
    }

    public ProgressListener getProgressListener() {
        return progressListener;
    }

    public IRequestListener getRequestListener() {
        return requestListener;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static class ImageSize {
        private int width = 0;
        private int height = 0;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class Builder {
        private int placeholder;
        private int errorDrawable;
        private ImageSize imageSize;
        private boolean isCrossFade;
        @ResourceType
        private int resourceType = ResourceType.DRAWABLE;
        @ScaleType
        private int scaleType = ScaleType.FIT_CENTER;
        private boolean timeSignature;
        private ProgressListener progressListener;
        private IRequestListener requestListener;

        public Builder placeholder(@DrawableRes int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder error(@DrawableRes int errorId) {
            this.errorDrawable = errorId;
            return this;
        }

        public Builder scaleType(@ScaleType int scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Builder imageSize(int width, int height) {
            if (this.imageSize == null) {
                this.imageSize = new ImageSize(width, height);
            } else {
                this.imageSize.width = width;
                this.imageSize.height = height;
            }
            return this;
        }

        public Builder crossFade(boolean flag) {
            this.isCrossFade = flag;
            return this;
        }

        public Builder as(@ResourceType int resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder timeSignature(boolean timeSignature) {
            this.timeSignature = timeSignature;
            return this;
        }

        public Builder progressListener(ProgressListener progressListener) {
            this.progressListener = progressListener;
            return this;
        }

        public <T> Builder listener(IRequestListener<T> requestListener) {
            this.requestListener = requestListener;
            return this;
        }

        public ImageLoaderOptions build() {
            return new ImageLoaderOptions(this);
        }
    }
}
