package ext.arch.components.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ext.arch.components.R;

public class VerticalTextView extends View {
    public static final int ORIENTATION_START = 0;
    public static final int ORIENTATION_END = 1;

    public static final int LINE_NONE = 0;
    public static final int LINE_LEFT = 1;
    public static final int LINE_RIGHT = 2;


    @IntDef({ORIENTATION_START, ORIENTATION_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    @IntDef({LINE_NONE, LINE_RIGHT, LINE_LEFT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Line {
    }

    private float mTextSize;
    @ColorInt
    private int mTextColor = Color.BLACK;
    private String mText;
    @Orientation
    private int mOrientation = ORIENTATION_START;
    @Line
    private int mLine = LINE_NONE;
    private float mLineWidth;
    @ColorInt
    private int mLineColor = Color.BLACK;
    private String mSplitChars;
    private float mTextMarginHorizontal;
    private float mTextMarginVertical;
    private float mLine2TextMargin = -1;
    private int mGravity = Gravity.TOP | Gravity.START;

    private Paint mPaint;
    private int mWidth;
    private int mHeight = -1;
    private RectF mTextRectF = new RectF();

    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void initResource(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        if (typedArray != null) {
            mOrientation = typedArray.getInt(R.styleable.VerticalTextView_vOrientation, ORIENTATION_START);
            mText = typedArray.getString(R.styleable.VerticalTextView_android_text);
            mTextColor = typedArray.getColor(R.styleable.VerticalTextView_android_textColor, Color.BLACK);
            mTextSize = typedArray.getDimension(R.styleable.VerticalTextView_android_textSize, mTextSize);
            mSplitChars = typedArray.getString(R.styleable.VerticalTextView_vSplitChars);
            mTextMarginVertical = typedArray.getDimension(R.styleable.VerticalTextView_vTextMarginVertical, mTextMarginVertical);
            mTextMarginHorizontal = typedArray.getDimension(R.styleable.VerticalTextView_vTextMarginHorizontal, mTextMarginHorizontal);
            mLine = typedArray.getInt(R.styleable.VerticalTextView_vLine, LINE_NONE);
            mLineWidth = typedArray.getDimension(R.styleable.VerticalTextView_vLineWidth, mLineWidth);
            mLineColor = typedArray.getColor(R.styleable.VerticalTextView_vLineColor, Color.WHITE);
            mLine2TextMargin = mTextMarginHorizontal / 2 + mLineWidth / 2 - typedArray.getDimension(R.styleable.VerticalTextView_vLineTextGap, 0);
            mGravity = typedArray.getInt(R.styleable.VerticalTextView_android_gravity, mGravity);

            typedArray.recycle();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
        mLineWidth = dip2px(getContext(), 0.5f);
        mTextMarginHorizontal = dip2px(getContext(), 4);
        mTextMarginVertical = dip2px(getContext(), 3);
        initResource(context, attrs);
        mPaint = new Paint();
        if (mTextSize > 0) {
            mPaint.setTextSize(mTextSize);
        }
        mPaint.setColor(mTextColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = measureHeight(heightMeasureSpec);
        //此处修复relativelayout下存在的异常
        if (mHeight == -1) {
            mHeight = h;
        } else {
            if (mHeight > h) {
                mHeight = h;
            }
        }
        mWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public void setLine2TextMargin(float line2TextMargin) {
        this.mLine2TextMargin = mTextMarginHorizontal / 2 + mLineWidth / 2 - line2TextMargin;
        postInvalidate();
    }

    public void setLine(@Line int line) {
        this.mLine = line;
        postInvalidate();
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
        postInvalidate();
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        postInvalidate();
    }

    public void setTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
        postInvalidate();
    }

    public void setTextMarginHorizontal(float textMarginHorizontal) {
        this.mTextMarginHorizontal = textMarginHorizontal;
        postInvalidate();
    }

    public void setTextMarginVertical(float textMarginVertical) {
        this.mTextMarginVertical = textMarginVertical;
        postInvalidate();
    }

    public void setLineOrientation(@Line int lineOrientation) {
        this.mLine = lineOrientation;
        postInvalidate();
    }

    public void setSplitChars(String splitChars) {
        this.mSplitChars = splitChars;
        postInvalidate();
    }

    /**
     * 设置文字尺寸
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        postInvalidate();
    }

    /**
     * 设置文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        postInvalidate();
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        this.mText = text;
        postInvalidate();
    }

    /**
     * 设置文字起始方向
     *
     * @param orientation
     */
    public void setOrientation(@Orientation int orientation) {
        this.mOrientation = orientation;
        postInvalidate();
    }


    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            return (int) measureTextWidth();
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (getOneWordHeight() * mText.length());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private float measureTextWidth() {
        if (getColNum() == 1) {
            return getOneWordWidth() + getPaddingLeft() + getPaddingRight();
        }

        return getOneWordWidth() * getColNum() + getPaddingLeft() + getPaddingRight();
    }

    private float getTextBaseLine(RectF rect) {
        Paint.FontMetricsInt metricsInt = mPaint.getFontMetricsInt();
        return (rect.top + rect.bottom - metricsInt.top - metricsInt.bottom) / 2;
    }


    private int getColNum() {
        int oneRowWordCount = getColWordCount();
        int colNum = 0;
        if (mSplitChars != null) {
            String[] textArray = mText.split(mSplitChars);
            for (int i = 0; i < textArray.length; i++) {
                if (textArray[i].length() > oneRowWordCount) {
                    colNum += textArray[i].length() / oneRowWordCount;
                    if (textArray[i].length() % oneRowWordCount > 0) {
                        colNum++;
                    }
                } else {
                    colNum++;
                }
            }
        } else {
            colNum = mText.length() / oneRowWordCount;
            if (mText.length() % oneRowWordCount > 0) {
                colNum++;
            }
        }
        return colNum;
    }

    private float getOneWordWidth() {
        return mPaint.measureText("A") + mTextMarginHorizontal;
    }

    private float getOneWordHeight() {
        Rect rect = new Rect();
        mPaint.getTextBounds("A", 0, 1, rect);
        return rect.height() + mTextMarginVertical;
    }

    private int getColWordCount() {
        return (int) (mHeight / getOneWordHeight());
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int oneLineWordCount = getColWordCount();

        float w = getOneWordWidth();
        float h = getOneWordHeight();

        int colNum = getColNum();

        float totalTextWidth = w * colNum;
        float shiftLeft = 0;
        float shiftTop = 0;
        int gravityW = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int gravityH = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        if (gravityH == Gravity.CENTER_HORIZONTAL) {
            shiftLeft = (mWidth - totalTextWidth) / 2;
        }
        if (gravityW == Gravity.CENTER_VERTICAL) {
            // fix me.
        }

        final String[] cutCharArray = mSplitChars == null ? null : mSplitChars.split("|");
        if (cutCharArray != null) {
            String[] textArray = mText.split(mSplitChars);
            int stepCol = 0;
            for (int n = 0; n < textArray.length; n++) {
                String text = textArray[n];
                int currentCol = 0;
                for (int i = 0; i < text.length(); i++) {
                    String str = String.valueOf(text.charAt(i));
                    int currentRow = i % oneLineWordCount;
                    if (colNum == 1) {
                        currentRow = i;
                    }
                    if (colNum > 1) {
                        currentCol = stepCol + (i / oneLineWordCount);
                    }
                    drawText(w, h, currentCol, currentRow, shiftLeft, shiftTop, str, canvas);
                    if (i + 1 == text.length()) {
                        stepCol = currentCol + 1;
                    }
                }
            }
        } else {
            int currentCol = 0;
            for (int i = 0; i < mText.length(); i++) {
                String str = String.valueOf(mText.charAt(i));
                int currentRow = i % oneLineWordCount;
                if (colNum == 1) {
                    currentRow = i;
                }
                if (colNum > 1) {
                    currentCol = (i) / oneLineWordCount;
                }
                drawText(w, h, currentCol, currentRow, shiftLeft, shiftTop, str, canvas);
            }
        }
    }

    private void drawText(float w, float h, int currentCol, int currentRow, float shiftLeft, float shiftTop, String str, Canvas canvas) {
        float left;
        float top;
        if (mOrientation == ORIENTATION_START) {
            left = shiftLeft + currentCol * w;
            top = shiftTop + currentRow * h;
        } else {
            left = mWidth - shiftLeft - (currentCol + 1) * w;
            top = shiftTop + currentRow * h;
        }
        mTextRectF.set(left, top, left + w, top + h);
        float baseline = getTextBaseLine(mTextRectF);
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(str, mTextRectF.centerX(), baseline, mPaint);
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        if (mLine2TextMargin == -1) {
            mLine2TextMargin = mLineWidth * 1f / 2;
        }
        if (mLine == LINE_RIGHT) {
            Path path = new Path();
            path.moveTo(mTextRectF.right - mLine2TextMargin, mTextRectF.top);
            path.lineTo(mTextRectF.right - mLine2TextMargin, mTextRectF.bottom);
            canvas.drawPath(path, mPaint);
        } else if (mLine == LINE_LEFT) {
            Path path = new Path();
            path.moveTo(mTextRectF.left + mLine2TextMargin, mTextRectF.top);
            path.lineTo(mTextRectF.left + mLine2TextMargin, mTextRectF.bottom);
            canvas.drawPath(path, mPaint);
        }
    }
}
