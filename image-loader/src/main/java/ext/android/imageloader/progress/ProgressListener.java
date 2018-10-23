package ext.android.imageloader.progress;

/**
 * Created by LHEE on 2018/3/18.
 */

public interface ProgressListener {
    void update(String url, long bytesRead, long contentLength, boolean done);
}
