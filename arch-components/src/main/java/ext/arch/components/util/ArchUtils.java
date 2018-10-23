package ext.arch.components.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public final class ArchUtils {
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static boolean isApkDebuggable(@NonNull Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        return (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @ColorInt
    public static int getColorAccent(Context context, @ColorInt int defaultColor) {
        int colorAccent = 0xff000000;
        int[] attributes = {android.R.attr.colorAccent};
        TypedArray ta = context.obtainStyledAttributes(attributes);
        if (ta != null) {
            colorAccent = ta.getColor(0, defaultColor);
            ta.recycle();
        }
        return colorAccent;
    }
}
