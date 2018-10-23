package ext.arch.components.banner;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/3.
 */

class LoopPagerAdapterWrapper extends PagerAdapter {

    private final PagerAdapter mAdapter;
    private final SparseArrayCompat<ToDestroy> mToDestroy;
    private boolean mBoundaryCaching;

    LoopPagerAdapterWrapper(@NonNull PagerAdapter adapter) {
        this.mAdapter = adapter;
        this.mToDestroy = new SparseArrayCompat<>();
        this.mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                notifyDataSetChanged();
            }
        });
    }

    public PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
    }

    public int toInnerPosition(int realPosition) {
        int position = realPosition + 1;
        return position;
    }

    public int toRealPosition(int position) {
        final int realCount = getRealCount();
        if (realCount == 0) return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;
        return realPosition;
    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    private int getRealFirstPosition() {
        return 1;
    }

    private int getRealLastPosition() {
        return getRealFirstPosition() + getRealCount() - 1;
    }

    @Override
    public int getCount() {
        return getRealCount() == 0 ? 0 : getRealCount() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int realPosition = (mAdapter instanceof FragmentPagerAdapter
                || mAdapter instanceof FragmentStatePagerAdapter)
                ? position
                : toRealPosition(position);
        if (mBoundaryCaching) {
            ToDestroy toDestroy = mToDestroy.get(position);
            if (toDestroy != null) {
                mToDestroy.remove(position);
                return toDestroy.object;
            }
        }
        return mAdapter.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final int realFirst = getRealFirstPosition();
        final int realLast = getRealLastPosition();
        final int realPosition = (mAdapter instanceof FragmentPagerAdapter
                || mAdapter instanceof FragmentStatePagerAdapter)
                ? position
                : toRealPosition(position);
        if (mBoundaryCaching && (position == realFirst || position == realLast)) {
            mToDestroy.put(position, new ToDestroy(container, realPosition, object));
        } else {
            mAdapter.destroyItem(container, realPosition, object);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        mAdapter.finishUpdate(container);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        mAdapter.restoreState(state, loader);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

    static class ToDestroy {
        ViewGroup container;
        int position;
        Object object;

        ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }
}
