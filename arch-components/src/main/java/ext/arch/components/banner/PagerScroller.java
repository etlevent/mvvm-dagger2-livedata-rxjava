package ext.arch.components.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/7/4.
 */

public class PagerScroller extends Scroller {

    private int mScrollDuration = -1;
    private ViewPager mPager;

    public PagerScroller(Context context, ViewPager pager) {
        super(context);
        mPager = pager;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        if (mScrollDuration > 0 && dx == mPager.getMeasuredWidth())
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        else
            super.startScroll(startX, startY, dx, dy);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (mScrollDuration > 0 && dx == mPager.getMeasuredWidth())
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        else
            super.startScroll(startX, startY, dx, dy, duration);
    }

    public void setScrollDuration(int duration) {
        mScrollDuration = duration;
    }
}
