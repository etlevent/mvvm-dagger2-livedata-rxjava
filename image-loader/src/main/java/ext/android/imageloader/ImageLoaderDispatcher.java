package ext.android.imageloader;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RestrictTo;

import java.util.HashMap;
import java.util.Map;

import ext.android.imageloader.progress.ProgressListener;

/**
 * Created by LHEE on 2018/3/19.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class ImageLoaderDispatcher implements ProgressListener {

    private static volatile ImageLoaderDispatcher _instance;
    private final Map<Object, ProgressListener> mListeners;
    private final Handler mHandler;

    private ImageLoaderDispatcher() {
        mListeners = new HashMap<>(5);
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageLoaderDispatcher get() {
        if (_instance == null) {
            synchronized (ImageLoaderDispatcher.class) {
                if (_instance == null) {
                    _instance = new ImageLoaderDispatcher();
                }
            }
        }
        return _instance;
    }

    public void addProgressListener(Object model, ProgressListener listener) {
        if (!mListeners.containsKey(model)) {
            mListeners.put(model, listener);
        }
    }

    public void removeProgressListener(Object model) {
        if (mListeners.containsKey(model)) {
            mListeners.remove(model);
        }
    }

    @Override
    public void update(String url, long bytesRead, long contentLength, boolean done) {
        if (mListeners.get(url) != null) {
            mHandler.post(() -> mListeners.get(url).update(url, bytesRead, contentLength, done));
        }
    }
}
