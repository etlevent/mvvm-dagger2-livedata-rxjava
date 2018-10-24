package ext.arch.components.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ext.arch.components.R;
import ext.arch.components.compact.CompatUtils;
import ext.arch.components.util.ArchUtils;

public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    private TextView mContentView;
    private TextView mExpandView;
    private boolean mChanged;
    private boolean mExpanded;

    private int mMaxLines = 4;
    private int mExpandColor;
    private float mTextSize = 15;
    private int mTextColor = 0xcc000000;
    private float mLineSpacingMultiplier = 1.0f;
    private CharSequence mText;

    private Drawable mMoreDrawable;
    private Drawable mLessDrawable;

    private String mLabelExpandMore = "展开全部";
    private String mLabelExpandLess = "收起";

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initResource(context, attrs);
    }

    private void initResource(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        @ColorInt int colorAccent = ArchUtils.getColorAccent(context, 0);
        if (ta != null) {
            mMaxLines = ta.getInt(R.styleable.ExpandableTextView_android_maxLines, mMaxLines);
            mExpandColor = ta.getColor(R.styleable.ExpandableTextView_expandColor, colorAccent);
            mTextColor = ta.getColor(R.styleable.ExpandableTextView_android_textColor, mTextColor);
            mTextSize = ta.getDimension(R.styleable.ExpandableTextView_android_textSize, mTextSize);
            mLineSpacingMultiplier = ta.getFloat(R.styleable.ExpandableTextView_android_lineSpacingMultiplier, mLineSpacingMultiplier);
            mText = ta.getString(R.styleable.ExpandableTextView_android_text);
            ta.recycle();
        } else {
            mExpandColor = colorAccent;
        }
        mMoreDrawable = CompatUtils.INSTANCE.tint(context, R.drawable.arch_vector_expand_more, mExpandColor);
        mLessDrawable = CompatUtils.INSTANCE.tint(context, R.drawable.arch_vector_expand_less, mExpandColor);
        mLabelExpandMore = context.getString(R.string.arch_expand_more);
        mLabelExpandLess = context.getString(R.string.arch_expand_less);
    }

    private void initView() {
        mContentView = new AppCompatTextView(getContext());
        mContentView.setTextColor(mTextColor);
        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mContentView.setLineSpacing(0.0f, mLineSpacingMultiplier);
        mContentView.setText(mText);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(params);
        addView(mContentView, 0);

        mExpandView = new AppCompatTextView(getContext());
        mExpandView.setGravity(Gravity.RIGHT);
        mMoreDrawable.setBounds(0, 0, mExpandView.getLineHeight(), mExpandView.getLineHeight());
        mLessDrawable.setBounds(0, 0, mExpandView.getLineHeight(), mExpandView.getLineHeight());
        mExpandView.setCompoundDrawables(null, null, mMoreDrawable, null);
        mExpandView.setText(mLabelExpandMore);
        mExpandView.setTextColor(mExpandColor);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        mExpandView.setLayoutParams(params);
        mExpandView.setOnClickListener(this);
        addView(mExpandView, getChildCount());
        mChanged = true;
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation == HORIZONTAL) {
            throw new IllegalArgumentException("Only support VERTICAL layout!");
        }
        super.setOrientation(orientation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mChanged || getVisibility() == GONE) {
            return;
        }
        mChanged = false;
        mContentView.setMaxLines(Integer.MAX_VALUE);
        mContentView.setEllipsize(null);
        mExpandView.setVisibility(GONE);
        visibleExtraView(true);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int lineCount = mContentView.getLineCount();
        if (lineCount <= mMaxLines)
            return;
        mExpandView.setVisibility(View.VISIBLE);
        if (!mExpanded) {
            mContentView.setMaxLines(mMaxLines);
            mContentView.setEllipsize(TextUtils.TruncateAt.END);
            mExpandView.setCompoundDrawables(null, null, mMoreDrawable, null);
            mExpandView.setText(mLabelExpandMore);
            visibleExtraView(false);
        } else {
            mExpandView.setCompoundDrawables(null, null, mLessDrawable, null);
            mExpandView.setText(mLabelExpandLess);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setText(CharSequence text) {
        mChanged = true;
        mText = text;
        mContentView.setText(mText);
    }

    public void setText(@StringRes int resId) {
        mChanged = true;
        mText = getResources().getString(resId);
        mContentView.setText(resId);
    }

    @Override
    public void onClick(View v) {
        mExpanded = !mExpanded;
        mChanged = true;
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void visibleExtraView(boolean visible) {
        if (getChildCount() == 2)
            return;
        for (int i = 1; i < getChildCount() - 1; i++) {
            final View child = getChildAt(i);
            child.setVisibility(visible ? VISIBLE : GONE);
        }
    }
}
