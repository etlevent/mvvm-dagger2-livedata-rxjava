package ext.arch.components.banner.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2017/7/6.
 */

public class DefaultTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {// [-Infinity, -1)
            //左侧不可见状态
        } else if (position <= 0) {//[-1, 0]
            //选中状态左侧
        } else if (position <= 1) {//(0, 1]
            //选中状态右侧
        } else {// (1,+Infinity]
            //右侧不可见状态
        }
    }
}
