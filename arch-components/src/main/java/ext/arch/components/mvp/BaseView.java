package ext.arch.components.mvp;

import android.support.annotation.NonNull;

/**
 * @author roothost
 * @date 2018/1/15
 */

public interface BaseView<P extends BasePresenter> {
    void setPresenter(@NonNull P presenter);
}
