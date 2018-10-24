package ext.arch.components.compact

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.FileProvider
import java.io.File

/**
 * Created by roothost on 2017/12/6.
 */
private const val AUTHORITIES_SUFFIX: String = ".FILE_PROVIDER"

/**
 * uri转化成适应7.0的content://形式 针对图片格式
 */
fun grantUriPermissions(context: Context, intent: Intent, file: File): Intent {
    val uri = FileProvider.getUriForFile(context, context.packageName + AUTHORITIES_SUFFIX, file)
    val resolveInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    for (resolveInfo in resolveInfoList) {
        context.grantUriPermission(resolveInfo.activityInfo.packageName, uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    return intent
}

/**
 * 对content://请求临时授权
 */

fun grantUriPermissions(context: Context, intent: Intent, uri: Uri): Intent {
    val resolveInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    for (resolveInfo in resolveInfoList) {
        context.grantUriPermission(resolveInfo.activityInfo.packageName, uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    return intent
}

/**
 * 普通uri转化成适应7.0的content://形式
 */
fun createFileProviderUri(context: Context, file: File): Uri {
    return FileProvider.getUriForFile(context, context.packageName + AUTHORITIES_SUFFIX, file)
}