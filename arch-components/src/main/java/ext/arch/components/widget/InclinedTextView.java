package ext.arch.components.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ext.arch.components.R;

public class InclinedTextView extends View {

    public static final int ORIENTATION_LEFT_TOP = 0;
    public static final int ORIENTATION_LEFT_BOTTOM = 1;
    public static final int ORIENTATION_RIGHT_TOP = 2;
    public static final int ORIENTATION_RIGHT_BOTTOM = 3;

    @IntDef({ORIENTATION_LEFT_TOP,
            ORIENTATION_LEFT_BOTTOM,
            ORIENTATION_RIGHT_TOP,
            ORIENTATION_RIGHT_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {

    }

    public static final int MODE_TRAPEZOID = 0;
    public static final int MODE_TRIANGLE = 1;

    @IntDef({MODE_TRAPEZOID, MODE_TRIANGLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {

    }

    @Mode
    private int mMode = MODE_TRAPEZOID;
    @Orientation
    private int mOrientation = ORIENTATION_LEFT_TOP;
    private String mText;
    @ColorInt
    private int mTextColor = Color.BLACK;
    private int mTextSize = 12;
    @ColorInt
    private int mDiagonalColor = Color.LTGRAY;
    private float mDiagonalLength = 20;
    private float mDiagonalHeight = 10;


    public InclinedTextView(Context context) {
        this(context, null);
    }

    public InclinedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InclinedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InclinedTextView);
        if (ta != null) {
            mText = ta.getString(R.styleable.InclinedTextView_android_text);
            mTextColor = ta.getColor(R.styleable.InclinedTextView_android_textColor, mTextColor);
            mTextSize = ta.getDimensionPixelSize(R.styleable.InclinedTextView_android_textSize, mTextSize);
            mDiagonalColor = ta.getColor(R.styleable.InclinedTextView_diagonalColor, mDiagonalColor);
            mDiagonalLength = ta.getDimension(R.styleable.InclinedTextView_diagonalLength, mDiagonalLength);
            mDiagonalHeight = ta.getDimension(R.styleable.InclinedTextView_diagonalHeight, mDiagonalHeight);
            mMode = ta.getInt(R.styleable.InclinedTextView_mode, mMode);
            mOrientation = ta.getInt(R.styleable.InclinedTextView_orientation, mOrientation);
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
