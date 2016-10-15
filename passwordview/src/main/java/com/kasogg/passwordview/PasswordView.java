package com.kasogg.passwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class PasswordView extends EditText {
    private int mBorderColor = Color.GRAY;
    private float mBorderWidth = 1;
    private float mBorderRadius = 0;

    private int mPasswordColor = Color.BLACK;
    private float mPasswordPlaceholderRadius = 6;

    private int mMaxLength = 6;

    private Paint mBorderPaint;
    private Paint mPasswordPaint;
    private RectF mBorderRect;
    private int mTextFilledCount;

    private InputListener mInputListener;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, dm);
        mBorderRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderRadius, dm);
        mPasswordPlaceholderRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPasswordPlaceholderRadius, dm);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordView, 0, 0);
        mBorderColor = a.getColor(R.styleable.PasswordView_pvBorderColor, mBorderColor);
        mBorderWidth = a.getDimension(R.styleable.PasswordView_pvBorderWidth, mBorderWidth);
        mBorderRadius = a.getDimension(R.styleable.PasswordView_pvBorderRadius, mBorderRadius);
        mPasswordColor = a.getColor(R.styleable.PasswordView_pvPasswordColor, mPasswordColor);
        mPasswordPlaceholderRadius = a.getDimension(R.styleable.PasswordView_pvPasswordPlaceholderRadius, mPasswordPlaceholderRadius);
        mMaxLength = a.getInt(R.styleable.PasswordView_pvPasswordLength, mMaxLength);
        a.recycle();

        mBorderPaint = new Paint(ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);

        mPasswordPaint = new Paint(ANTI_ALIAS_FLAG);
        mPasswordPaint.setStyle(Paint.Style.FILL);
        mPasswordPaint.setColor(mPasswordColor);

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setCursorVisible(false);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = mBorderWidth / 2;
        mBorderRect = new RectF(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // Outside Rect
        mBorderPaint.setStrokeWidth(mBorderWidth);
        canvas.drawRoundRect(mBorderRect, mBorderRadius, mBorderRadius, mBorderPaint);

        // Divider
        float dividerWidth = mBorderWidth;
        float spaceWidth = (width - (dividerWidth * (mMaxLength + 1))) / mMaxLength;
        mBorderPaint.setStrokeWidth(dividerWidth);
        for (int i = 1; i < mMaxLength; i++) {
            float x = dividerWidth / 2 + (spaceWidth + dividerWidth) * i;
            canvas.drawLine(x, 0, x, height, mBorderPaint);
        }

        // Password Circle
        float cx;
        float cy = height / 2;
        for (int i = 0; i < mTextFilledCount; i++) {
            cx = dividerWidth + spaceWidth / 2 + (spaceWidth + dividerWidth) * i;
            canvas.drawCircle(cx, cy, mPasswordPlaceholderRadius, mPasswordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mTextFilledCount = text.toString().length();
        invalidate();
        if (mTextFilledCount == mMaxLength && mInputListener != null) {
            mInputListener.onFinish(getText().toString());
        }
    }

    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        mBorderPaint.setColor(borderColor);
        invalidate();
    }

    public void setBorderWidth(float borderWidth) {
        this.mBorderWidth = borderWidth;
        mBorderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public void setBorderRadius(float borderRadius) {
        this.mBorderRadius = borderRadius;
        invalidate();
    }

    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
        invalidate();
    }

    public void setPasswordColor(int passwordColor) {
        this.mPasswordColor = passwordColor;
        mPasswordPaint.setColor(passwordColor);
        invalidate();
    }

    public void setPasswordPlaceholderRadius(float passwordPlaceholderRadius) {
        this.mPasswordPlaceholderRadius = passwordPlaceholderRadius;
        mPasswordPaint.setStrokeWidth(passwordPlaceholderRadius);
        invalidate();
    }

    public void setInputCallback(InputListener listener) {
        mInputListener = listener;
    }

    public interface InputListener {
        void onFinish(String password);
    }
}