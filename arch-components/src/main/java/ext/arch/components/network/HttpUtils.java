package ext.arch.components.network;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

public final class HttpUtils {
    private HttpUtils() {
        throw new AssertionError("no instance.");
    }

    static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
        // Android (http://b.android.com/58753) but it forces composition of API declarations which is
        // the recommended pattern.
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }

    public static String coverUpEmptyPath(@NonNull String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        } else {
            return url;
        }
    }

    public static String checkUrl(@NonNull String url) {
        Uri uri = Uri.parse(url);
        if (uri == null) {
            throw new IllegalArgumentException("uri parse error.");
        }
        final List<String> paths = uri.getPathSegments();
        if (paths == null || paths.isEmpty()) {
            return url;
        }
        if (url.endsWith("/")) {
            return url;
        } else {
            return url + "/";
        }
    }
}
