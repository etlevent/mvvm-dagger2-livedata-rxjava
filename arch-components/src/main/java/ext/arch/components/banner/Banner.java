package ext.arch.components.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ext.arch.components.R;

/**
 * Created by Administrator on 2017/7/3.
 */

public class Banner extends ViewGroup {

    public static final int NONE_INDICATOR = 0;
    public static final int CIRCLE_INDICATOR = 1;
    public static final int NUMBER_INDICATOR = 2;
    public static final int TITLE_NUMBER_INDICATOR = 3;
    public static final int TITLE_CIRCLE_INDICATOR = 4;
    public static final int TITLE_CIRCLE_INDICATOR_INSIDE = 5;
    public static final int GRAVITY_LEFT = 6;
    public static final int GRAVITY_CENTER = 7;
    public static final int GRAVITY_RIGHT = 8;
    private static final String TAG = "Banner";
    private final Handler mHandler = new Handler();
    private LoopViewPager mLoopViewPager;
    private LinearLayout mTitleLayout;
    private TextView mTitleView;
    private TextView mNumberIndicator;
    private CircleIndicator mCircleIndicator;
    private int mBannerDuration = 2000;
    private int mTitleHeight = 80;
    private int mTitlePadding = 10;
    @DrawableRes
    private int mTitleBackground = R.drawable.arch_drawable_white_44;
    private int mNumberIndicatorSize = 80;
    @DrawableRes
    private int mNumberIndicatorBackground = R.drawable.arch_default_number_indicator;
    private int mPagerScrollDuration = 800;
    @ColorInt
    private int mTextColor = Color.WHITE;
    private float mTextSize = 12;
    private int mIndicatorGap = 5;
    private int mIndicatorPadding = 10;
    private int mIndicatorWidth = 10;
    private int mIndicatorHeight = 10;
    @DrawableRes
    private int mIndicatorDrawableRes = R.drawable.arch_default_indicator;
    private boolean mIsAutoPlay;
    private boolean mNoneScrollIfNeeded = true;
    @IndicatorStyle
    private int mIndicatorStyle = CIRCLE_INDICATOR;
    @IndicatorGravity
    private int mIndicatorGravity = GRAVITY_CENTER;
    private BannerSettings mSettings;
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        private int oldPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "[onPageSelected] position=" + position + ',' + oldPosition);
            onBannerPageSelected(position, oldPosition);
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private Runnable mBannerRunnable = new Runnable() {
        @Override
        public void run() {
            final int current = mLoopViewPager.getCurrentItem();
            mLoopViewPager.setCurrentItem(current + 1);
            mHandler.postDelayed(this, mBannerDuration);
        }
    };

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
        setIndicatorStyle(mIndicatorStyle);
    }

    private static void removeFromParentSafety(View view) {
        if (view == null) return;
        ViewParent parent = view.getParent();
        if (parent == null) return;
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(view);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        if (ta != null) {
            mIndicatorGap = ta.getDimensionPixelSize(R.styleable.Banner_indicatorGap, mIndicatorGap);
            mIndicatorPadding = ta.getDimensionPixelSize(R.styleable.Banner_indicatorPadding, mIndicatorPadding);
            mIndicatorWidth = ta.getDimensionPixelSize(R.styleable.Banner_indicatorWidth, mIndicatorWidth);
            mIndicatorHeight = ta.getDimensionPixelSize(R.styleable.Banner_indicatorHeight, mIndicatorHeight);
            mIndicatorDrawableRes = ta.getResourceId(R.styleable.Banner_indicatorDrawable, mIndicatorDrawableRes);

            mNumberIndicatorSize = ta.getDimensionPixelSize(R.styleable.Banner_numIndicatorSize, mNumberIndicatorSize);
            mNumberIndicatorBackground = ta.getResourceId(R.styleable.Banner_numIndicatorBackground, mNumberIndicatorBackground);

            mTitleHeight = ta.getDimensionPixelSize(R.styleable.Banner_titleHeight, mTitleHeight);
            mTitlePadding = ta.getDimensionPixelSize(R.styleable.Banner_titlePadding, mTitlePadding);
            mTitleBackground = ta.getResourceId(R.styleable.Banner_titleBackground, mTitleBackground);

            mTextSize = ta.getDimension(R.styleable.Banner_android_textSize, mTextSize);
            mTextColor = ta.getColor(R.styleable.Banner_android_textColor, mTextColor);

            mPagerScrollDuration = ta.getInt(R.styleable.Banner_pagerScrollDuration, mPagerScrollDuration);
            mBannerDuration = ta.getInt(R.styleable.Banner_bannerDuration, mBannerDuration);

            mIndicatorStyle = ta.getInt(R.styleable.Banner_indicatorStyle, mIndicatorStyle);
            mIndicatorGravity = ta.getInt(R.styleable.Banner_indicatorGravity, mIndicatorGravity);
            ta.recycle();
        }
    }

    private void init() {
        mLoopViewPager = new LoopViewPager(getContext(), mPagerScrollDuration);
        //todo: before fix boundary confused, keep flag with false
        mLoopViewPager.setBoundaryCaching(false);
        mLoopViewPager.addOnPageChangeListener(mPageChangeListener);
        addView(mLoopViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setAutoPlay(true);
    }

    public void setAdapter(@NonNull PagerAdapter adapter) {
        mLoopViewPager.setAdapter(adapter);
        mLoopViewPager.setOnDataChangedListener(this::populateFromPagerAdapter);
        populateFromPagerAdapter();
    }

    private void populateFromPagerAdapter() {
        stopAutoPlay();
        createTitles();
        createIndicator();
        mLoopViewPager.setCurrentItem(0);
        onBannerPageSelected(0, -1);
        if (mNoneScrollIfNeeded) {
            final boolean singleOrEmpty = mLoopViewPager.getAdapter().getCount() < 2;
            mLoopViewPager.setScrollable(!singleOrEmpty);
            if (mCircleIndicator != null) {
                mCircleIndicator.setVisibility(singleOrEmpty ? GONE : VISIBLE);
            }
            startAutoPlay();
        }
    }

    private void setAutoPlay(boolean isAutoPlay) {
        this.mIsAutoPlay = isAutoPlay;
    }

    private void setIndicatorStyle(@IndicatorStyle int style) {
        this.mIndicatorStyle = style;
    }

    private void setIndicatorGravity(@IndicatorGravity int gravity) {
        this.mIndicatorGravity = gravity;
    }

    private void setTitleHeight(int height) {
        mTitleHeight = height;
    }

    private void setTitlePadding(int padding) {
        mTitlePadding = padding;
    }

    private void setTitleBackground(@DrawableRes int resId) {
        mTitleBackground = resId;
    }

    private void setNumberIndicatorSize(int size) {
        mNumberIndicatorSize = size;
    }

    private void setNumberIndicatorBackground(@DrawableRes int resId) {
        mNumberIndicatorBackground = resId;
    }

    private void setTextColor(@ColorInt int color) {
        mTextColor = color;
    }

    private void setTextSize(int size) {
        mTextSize = size;
    }

    private void setIndicatorGap(int gap) {
        mIndicatorGap = gap;
    }

    private void setIndicatorPadding(int padding) {
        mIndicatorPadding = padding;
    }

    private void setIndicatorWidth(int width) {
        mIndicatorWidth = width;
    }

    private void setIndicatorHeight(int height) {
        mIndicatorHeight = height;
    }

    private void setIndicatorDrawableRes(@DrawableRes int resId) {
        mIndicatorDrawableRes = resId;
    }

    public void setPagerScrollDuration(int duration) {
        mPagerScrollDuration = duration;
        mLoopViewPager.setPagerScrollDuration(mPagerScrollDuration);
    }

    public void setBannerTransformer(@NonNull ViewPager.PageTransformer pageTransformer) {
        mLoopViewPager.setPageTransformer(true, pageTransformer);
    }

    private void setBannerDuration(int duration) {
        mBannerDuration = duration;
    }

    public BannerSettings getSettings() {
        if (mSettings == null) {
            mSettings = new BannerSettings(this);
        }
        return mSettings;
    }

    private void createTitles() {
        final int count = mLoopViewPager.getAdapter().getCount();
        if (count != 0 && mTitleLayout == null) {
            mTitleLayout = new LinearLayout(getContext());
            mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
            mTitleLayout.setBackgroundResource(mTitleBackground);
            mTitleLayout.setPadding(mTitlePadding, 0, mTitlePadding, 0);

            mTitleView = new AppCompatTextView(getContext());
            mTitleView.setTextColor(mTextColor);
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mTitleView.setGravity(Gravity.CENTER_VERTICAL);
            mTitleView.setMaxLines(1);
            mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.gravity = Gravity.CENTER_VERTICAL;
            mTitleLayout.addView(mTitleView, lp);
        }
        if (mIndicatorStyle == TITLE_NUMBER_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR_INSIDE) {
            if (mTitleLayout != null && !isViewAdded(mTitleLayout))
                addView(mTitleLayout, new LayoutParams(LayoutParams.MATCH_PARENT, mTitleHeight));
        } else {
            removeFromParentSafety(mTitleLayout);
        }
    }

    private void createIndicator() {
        removeFromParentSafety(mNumberIndicator);
        removeFromParentSafety(mCircleIndicator);
        if (mIndicatorStyle == NUMBER_INDICATOR
                || mIndicatorStyle == TITLE_NUMBER_INDICATOR) {
            if (mNumberIndicator == null) {
                mNumberIndicator = new AppCompatTextView(getContext());
            }
            mNumberIndicator.setTextColor(mTextColor);
            mNumberIndicator.setGravity(Gravity.CENTER);
            if (mIndicatorStyle == NUMBER_INDICATOR) {
                mNumberIndicator.setBackgroundResource(mNumberIndicatorBackground);
                if (mNumberIndicator.getParent() != null) {
                    LayoutParams lp = mNumberIndicator.getLayoutParams();
                    lp.width = lp.height = mNumberIndicatorSize;
                } else {
                    addView(mNumberIndicator, new LayoutParams(mNumberIndicatorSize, mNumberIndicatorSize));
                }
            } else {
                ViewCompat.setBackground(mNumberIndicator, null);
                mTitleLayout.addView(mNumberIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            }
            return;
        }
        if (mCircleIndicator == null) {
            mCircleIndicator = new CircleIndicator(getContext());
        }
        mCircleIndicator.setGap(mIndicatorGap);
        mCircleIndicator.setIndicatorPadding(mIndicatorPadding);
        mCircleIndicator.setIndicatorWidth(mIndicatorWidth);
        mCircleIndicator.setIndicatorHeight(mIndicatorHeight);
        mCircleIndicator.setIndicatorDrawableRes(mIndicatorDrawableRes);
        final int count = mLoopViewPager.getAdapter().getCount();
        mCircleIndicator.setCount(count);
        if (mIndicatorStyle == CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR) {
            addView(mCircleIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        } else if (mIndicatorStyle == TITLE_CIRCLE_INDICATOR_INSIDE) {
            if (mTitleLayout == null)
                throw new IllegalStateException("cannot get a title container, please check titles");
            mTitleLayout.addView(mCircleIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (mLoopViewPager.getAdapter() == null)
            return;
        final int pageCount = mLoopViewPager.getAdapter().getCount();
        if (pageCount == 0)
            return;
        if (getVisibility() == GONE
                || mLoopViewPager.getVisibility() == GONE)
            return;
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int parentWidth = getMeasuredWidth();
        final int parentHeight = getMeasuredHeight();
        Log.i(TAG, "[onLayout] " + paddingLeft + ", " + paddingRight + ", " + paddingTop + ", " + paddingBottom);
        Log.i(TAG, "[onLayout] " + parentWidth + "x" + parentHeight);
        Log.i(TAG, "[viewPager] " + mLoopViewPager.getMeasuredWidth() + "," + mLoopViewPager.getMeasuredHeight());
        mLoopViewPager.layout(paddingLeft, paddingTop,
                parentWidth - paddingRight + paddingLeft,
                parentHeight - paddingBottom + paddingTop);

        layoutIndicator();
    }

    private void layoutIndicator() {
        if (mIndicatorStyle == NONE_INDICATOR) {
            layoutNoneStyle();
            return;
        }
        final int paddingLeft = getPaddingLeft();
        //final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int parentWidth = getMeasuredWidth();
        final int parentHeight = getMeasuredHeight();
        if (mIndicatorStyle == TITLE_CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_NUMBER_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR_INSIDE) {
            if (mTitleLayout != null) {
                int left = paddingLeft;
                int top = parentHeight - mTitleLayout.getMeasuredHeight() - paddingBottom;
                int right = parentWidth - paddingRight + paddingLeft;
                int bottom = top + mTitleLayout.getMeasuredHeight();
                mTitleLayout.layout(left, top, right, bottom);
            }
        }
        if (mIndicatorStyle == CIRCLE_INDICATOR
                || mIndicatorStyle == TITLE_CIRCLE_INDICATOR) {
            int left = (parentWidth - mCircleIndicator.getMeasuredWidth()) / 2;
            if (mIndicatorGravity == GRAVITY_LEFT) {
                left = 0;
            } else if (mIndicatorGravity == GRAVITY_RIGHT) {
                left = parentWidth - mCircleIndicator.getMeasuredWidth();
            }
            int top = parentHeight - mCircleIndicator.getMeasuredHeight() - paddingBottom;
            int right = left + mCircleIndicator.getMeasuredWidth();
            int bottom = top + mCircleIndicator.getMeasuredHeight();
            if (mIndicatorStyle == TITLE_CIRCLE_INDICATOR
                    && mTitleLayout != null) {
                top -= mTitleLayout.getMeasuredHeight();
                bottom = top + mCircleIndicator.getMeasuredHeight();
            }
            mCircleIndicator.layout(left, top, right, bottom);
        }

        if (mIndicatorStyle == NUMBER_INDICATOR) {
            int left = parentWidth - 10 - mNumberIndicator.getMeasuredWidth();
            int top = parentHeight - mNumberIndicator.getMeasuredHeight() - 10;
            int right = left + mNumberIndicator.getMeasuredWidth();
            int bottom = top + mNumberIndicator.getMeasuredHeight();
            mNumberIndicator.layout(left, top, right, bottom);
        }
    }

    private void layoutNoneStyle() {
        if (isViewAdded(mTitleLayout)) {
            removeView(mTitleLayout);
        }
        if (isViewAdded(mCircleIndicator)) {
            removeView(mCircleIndicator);
        }
        if (isViewAdded(mNumberIndicator)) {
            removeView(mNumberIndicator);
        }
    }

    private boolean isViewAdded(View view) {
        if (view != null && indexOfChild(view) != -1)
            return true;
        return false;
    }

    /**
     * Returns the index of the child to draw for this iteration. Override this
     * if you want to change the drawing order of children. By default, it
     * returns i.
     * <p>
     * NOTE: In order for this method to be called, you must enable child ordering
     * first by calling {@link #setChildrenDrawingOrderEnabled(boolean)}.
     *
     * @param i The current iteration.
     * @return The index of the child to draw this iteration.
     * @see #setChildrenDrawingOrderEnabled(boolean)
     * @see #isChildrenDrawingOrderEnabled()
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
//        if (mCircleIndicator == null)
//            return super.getChildDrawingOrder(childCount, i);
//        final int indicatorIndex = indexOfChild(mCircleIndicator);
//        if (indicatorIndex == -1)
//            return super.getChildDrawingOrder(childCount, i);
//        if (i == indicatorIndex) {
//            return childCount - 1;
//        } else if (i == childCount - 1) {
//            return indicatorIndex;
//        }
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsAutoPlay) {
            final int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
        Log.e(TAG, "[onDetachedFromWindow]");
    }

    private void onBannerPageSelected(int position, int oldPosition) {
        final int count = mLoopViewPager.getAdapter().getCount();
        if (mCircleIndicator != null)
            mCircleIndicator.onPageSelected(position, oldPosition);
        if (count == 0) return;
        if (mTitleView != null)
            mTitleView.setText(mLoopViewPager.getAdapter().getPageTitle(position));
        if (mNumberIndicator != null) {
            int number = position + 1;
            mNumberIndicator.setText(number + "/" + count);
        }
    }

    private void startAutoPlay() {
        mHandler.removeCallbacks(mBannerRunnable);
        final boolean singleOrEmpty = mLoopViewPager.getAdapter().getCount() < 2;
        if (!singleOrEmpty && mNoneScrollIfNeeded) {
            mHandler.postDelayed(mBannerRunnable, mBannerDuration);
        }
    }

    private void stopAutoPlay() {
        mHandler.removeCallbacks(mBannerRunnable);
    }

    @IntDef({NONE_INDICATOR,
            CIRCLE_INDICATOR,
            NUMBER_INDICATOR,
            TITLE_NUMBER_INDICATOR,
            TITLE_CIRCLE_INDICATOR,
            TITLE_CIRCLE_INDICATOR_INSIDE})
    @Retention(RetentionPolicy.SOURCE)
    @interface IndicatorStyle {

    }

    @IntDef({GRAVITY_LEFT,
            GRAVITY_CENTER,
            GRAVITY_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface IndicatorGravity {
    }

    public static class BannerSettings {
        private Banner banner;

        private BannerSettings(@NonNull Banner banner) {
            this.banner = banner;
        }

        public BannerSettings setAutoPlay(boolean isAutoPlay) {
            this.banner.setAutoPlay(isAutoPlay);
            return this;
        }

        public BannerSettings setIndicatorStyle(@IndicatorStyle int style) {
            this.banner.setIndicatorStyle(style);
            return this;
        }

        public BannerSettings setIndicatorGravity(@IndicatorGravity int gravity) {
            this.banner.setIndicatorGravity(gravity);
            return this;
        }

        public BannerSettings setTitleHeight(int height) {
            this.banner.setTitleHeight(height);
            return this;
        }

        public BannerSettings setTitlePadding(int padding) {
            this.banner.setTitlePadding(padding);
            return this;
        }

        public BannerSettings setTitleBackground(@DrawableRes int resId) {
            this.banner.setTitleBackground(resId);
            return this;
        }

        public BannerSettings setNumberIndicatorSize(int size) {
            this.banner.setNumberIndicatorSize(size);
            return this;
        }

        public BannerSettings setNumberIndicatorBackground(@DrawableRes int resId) {
            this.banner.setNumberIndicatorBackground(resId);
            return this;
        }

        public BannerSettings setTextColor(@ColorInt int color) {
            this.banner.setTextColor(color);
            return this;
        }

        public BannerSettings setTextSize(int size) {
            this.banner.setTextSize(size);
            return this;
        }

        public BannerSettings setIndicatorGap(int gap) {
            this.banner.setIndicatorGap(gap);
            return this;
        }

        public BannerSettings setIndicatorPadding(int padding) {
            this.banner.setIndicatorPadding(padding);
            return this;
        }

        public BannerSettings setIndicatorWidth(int width) {
            this.banner.setIndicatorWidth(width);
            return this;
        }

        public BannerSettings setIndicatorHeight(int height) {
            this.banner.setIndicatorHeight(height);
            return this;
        }

        public BannerSettings setIndicatorDrawableRes(@DrawableRes int resId) {
            this.banner.setIndicatorDrawableRes(resId);
            return this;
        }

        public BannerSettings setBannerDuration(int duration) {
            this.banner.setBannerDuration(duration);
            return this;
        }
    }
}
