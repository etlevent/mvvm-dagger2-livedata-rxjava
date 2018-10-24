package ext.arch.components.compact

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import java.io.File


/**
 * Created by roothost on 2017/12/6.
 */
object CompatUtils {
    fun createCaptureIntent(context: Context, file: File): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri: Uri = createCompatUri(context, file)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        return intent
    }

    fun createCropIntent(context: Context, uri: Uri): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermissions(context, intent, uri)
        }
        // 设置裁剪
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320)
        intent.putExtra("outputY", 320)
        intent.putExtra("return-data", true)
        return intent
    }

    fun createCompatUri(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createFileProviderUri(context, file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * VectorDrawableCompat.create
     * more bugs.
     *
     * 1.gradle add
     * defaultConfig {
     * vectorDrawables.useSupportLibrary = true
     * }
     * 2.Activity Add static block
     * static {
     * AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
     * }
     * 3.ImageView or ImageButton can use Vector with app:srcCompat="@drawable/your_vector"
     * 4.TextView or Drawable should wrap with a drawable xml.
     * exp:
     * <pre>
     * &lt;selector xmlns:android=&quot;...&quot;&gt;
     * &lt;item android:state_checked=&quot;true&quot;
     * android:drawable=&quot;@drawable/vector_checked_icon&quot; /&gt;
     * &lt;item android:drawable=&quot;@drawable/vector_icon&quot; /&gt;
     * &lt;/selector&gt;
     *
     * &lt;TextView
     * ...
     * android:drawableLeft=&quot;@drawable/vector_state_list_icon&quot; /&gt;
    </pre> *
     * @param context
     * @param id
     * @return
     */

    fun getVectorDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return VectorDrawableCompat.create(context.resources, id, context.theme)
    }

    fun getVectorDrawable(context: Context, @DrawableRes id: Int, @ColorInt tint: Int): Drawable {
        var drawable = getVectorDrawable(context, id)
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable!!, tint)
        return drawable
    }

    fun tint(context: Context, @DrawableRes id: Int, @ColorInt tint: Int): Drawable {
        var drawable = ContextCompat.getDrawable(context, id)
        return tint(drawable!!, tint)
    }

    fun tint(drawable: Drawable, @ColorInt tint: Int): Drawable {
        var wrap = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrap, tint)
        return wrap
    }
}