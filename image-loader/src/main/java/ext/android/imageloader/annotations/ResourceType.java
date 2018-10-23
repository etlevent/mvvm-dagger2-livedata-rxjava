package ext.android.imageloader.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by roothost on 2018/3/19.
 */
@IntDef({ResourceType.DRAWABLE,
        ResourceType.BITMAP,
        ResourceType.GIF})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface ResourceType {
    int DRAWABLE = 0;
    int BITMAP = 1;
    int GIF = 2;
}
