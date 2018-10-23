package ext.android.imageloader.annotations;


import android.graphics.Matrix;
import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by roothost on 2018/3/16.
 */
@IntDef({ScaleType.MATRIX,
        ScaleType.FIT_XY,
        ScaleType.FIT_START,
        ScaleType.FIT_CENTER,
        ScaleType.FIT_END,
        ScaleType.CENTER,
        ScaleType.CENTER_CROP,
        ScaleType.CENTER_INSIDE})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface ScaleType {
    /**
     * Scale using the image matrix when drawing. The image matrix can be set using
     * {@link android.widget.ImageView#setImageMatrix(android.graphics.Matrix)} . From XML, use this syntax:
     * <code>android:scaleType="matrix"</code>.
     */
    int MATRIX = 0;
    /**
     * Scale the image using {@link android.graphics.Matrix.ScaleToFit#FILL}.
     * From XML, use this syntax: <code>android:scaleType="fitXY"</code>.
     */
    int FIT_XY = 1;
    /**
     * Scale the image using {@link android.graphics.Matrix.ScaleToFit#START}.
     * From XML, use this syntax: <code>android:scaleType="fitStart"</code>.
     */
    int FIT_START = 2;
    /**
     * Scale the image using {@link android.graphics.Matrix.ScaleToFit#CENTER}.
     * From XML, use this syntax:
     * <code>android:scaleType="fitCenter"</code>.
     */
    int FIT_CENTER = 3;
    /**
     * Scale the image using {@link android.graphics.Matrix.ScaleToFit#END}.
     * From XML, use this syntax: <code>android:scaleType="fitEnd"</code>.
     */
    int FIT_END = 4;
    /**
     * Center the image in the view, but perform no scaling.
     * From XML, use this syntax: <code>android:scaleType="center"</code>.
     */
    int CENTER = 5;
    /**
     * Scale the image uniformly (maintain the image's aspect ratio) so
     * that both dimensions (width and height) of the image will be equal
     * to or larger than the corresponding dimension of the view
     * (minus padding). The image is then centered in the view.
     * From XML, use this syntax: <code>android:scaleType="centerCrop"</code>.
     */
    int CENTER_CROP = 6;
    /**
     * Scale the image uniformly (maintain the image's aspect ratio) so
     * that both dimensions (width and height) of the image will be equal
     * to or less than the corresponding dimension of the view
     * (minus padding). The image is then centered in the view.
     * From XML, use this syntax: <code>android:scaleType="centerInside"</code>.
     */
    int CENTER_INSIDE = 7;

}
