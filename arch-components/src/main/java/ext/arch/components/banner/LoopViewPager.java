package ext.arch.components.banner;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class LoopViewPager extends ViewPager {

    private LoopPagerAdapterWrapper mAdapterWrapper;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private OnPageChangeListener mOnPageChangeListener;
    private OnDataChangedListener mOnDataChangedListener;
    private boolean mBoundaryCaching;
    private PagerScroller mScroller;
    private boolean mScrollable;
    private OnPageChangeListener mWrapperPageChangeListener = new OnPageChangeListener() {
        private float previousOffset = -1;
        private float previousPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapterWrapper != null) {
                realPosition = mAdapterWrapper.toRealPosition(position);
                if (positionOffset == 0 && previousOffset == 0
                        && (position == 0 || position == mAdapterWrapper.getCount() - 1)) {
                    setCurrentItem(realPosition);
                }
            }
            previousOffset = positionOffset;
            if (realPosition != mAdapterWrapper.getRealCount() - 1) {
                notifyOnPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            } else {
                if (positionOffset > 0.5f) {
                    notifyOnPageScrolled(0, 0, 0);
                } else {
                    notifyOnPageScrolled(realPosition, 0, 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            final int realPosition = mAdapterWrapper.toRealPosition(position);
            if (previousPosition != realPosition) {
                previousPosition = realPosition;
                notifyOnPageSelected(realPosition);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapterWrapper != null) {
                final int position = LoopViewPager.super.getCurrentItem();
                final int realPosition = mAdapterWrapper.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapterWrapper.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            notifyOnPageScrollStateChanged(state);
        }
    };
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (mOnDataChangedListener != null) {
                mOnDataChangedListener.onDataChanged();
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            if (mOnDataChangedListener != null) {
                mOnDataChangedListener.onDataChanged();
            }
        }
    };

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.addOnPageChangeListener(mWrapperPageChangeListener);
    }

    public LoopViewPager(Context context, int scrollDuration) {
        this(context, null);
        mScrollable = true;
        setOverScrollMode(OVER_SCROLL_NEVER);
        mScroller = new PagerScroller(context, this);
        mScroller.setScrollDuration(scrollDuration);
        setPagerScroller(mScroller);
    }

    public void setPagerScrollDuration(int duration) {
        mScroller.setScrollDuration(duration);
    }

    private void setPagerScroller(Scroller scroller) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, scroller);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapterWrapper != null)
            mAdapterWrapper.setBoundaryCaching(mBoundaryCaching);
    }

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.mOnDataChangedListener = onDataChangedListener;
    }

    public void setScrollable(boolean flag) {
        this.mScrollable = flag;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !mScrollable || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !mScrollable || super.onTouchEvent(ev);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapterWrapper != null ? mAdapterWrapper.getRealAdapter() : null;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (mAdapterWrapper != null) {
            mAdapterWrapper.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapterWrapper = new LoopPagerAdapterWrapper(adapter);
        mAdapterWrapper.setBoundaryCaching(mBoundaryCaching);
        mAdapterWrapper.registerDataSetObserver(mDataSetObserver);
        super.setAdapter(mAdapterWrapper);
        if (adapter.getCount() > 0) {
            setCurrentItem(0, false);
        }
    }

    @Override
    public int getCurrentItem() {
        return mAdapterWrapper != null ? mAdapterWrapper.toRealPosition(super.getCurrentItem()) : 0;
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapterWrapper.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners != null)
            mOnPageChangeListeners.remove(listener);
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null)
            mOnPageChangeListeners.clear();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    private void notifyOnPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null)
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        if (mOnPageChangeListeners != null)
            for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                mOnPageChangeListeners.get(i).onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
    }

    private void notifyOnPageSelected(int position) {
        if (mOnPageChangeListener != null)
            mOnPageChangeListener.onPageSelected(position);
        if (mOnPageChangeListeners != null)
            for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                mOnPageChangeListeners.get(i).onPageSelected(position);
            }
    }

    private void notifyOnPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        if (mOnPageChangeListeners != null) {
            for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                mOnPageChangeListeners.get(i).onPageScrollStateChanged(state);
            }
        }
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }
}
