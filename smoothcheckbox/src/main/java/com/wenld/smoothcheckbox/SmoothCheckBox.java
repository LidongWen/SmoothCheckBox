package com.wenld.smoothcheckbox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import com.wenld.customviewsupport.CustomView;
import com.wenld.customviewsupport.DensityUtils;

/**
 * <p/>
 * Author: 温利东 on 2017/2/23 15:36.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class SmoothCheckBox extends CustomView implements Checkable {
    private static final String KEY_INSTANCE_STATE = "SmoothCheckBox";  //状态保存

    private static final int COLOR_TICK = Color.WHITE;
    private static final int COLOR_UNCHECKED = Color.WHITE;
    private static final int COLOR_CHECKED = Color.parseColor("#FB4846");
    private static final int COLOR_OUT_UNCHECKED = Color.parseColor("#DFDFDF");

    private static final int DEF_DRAW_SIZE = 25;
    private static final int DEF_ANIM_DURATION = 300;

    private Paint mInPaint, mTickPaint, mOutPaint;
    private int inColor, tickColor, outColor;

    private Point mCenterPoint;
    private Path mTickPath, mTickdstPath;
    PathMeasure measure;
    Point tickStartPoint;
    float length;

    private int mCheckedColor, mUnCheckedColor, mFloorUnCheckedColor;

    private float mOutScale = 1.0f, mInScale = 1.0f, mTickScale = 1.0f; // 外圆缩放比例,内圆缩放比例,勾的进度比例
    private float mStrokeWidth;//边框的厚度

    private int mAnimDuration;

    private boolean mChecked;
    private SmoothCheckBox.OnCheckedChangeListener mListener;

    public SmoothCheckBox(Context context) {
        super(context);
    }

    public SmoothCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_INSTANCE_STATE, isChecked());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            boolean isChecked = bundle.getBoolean(KEY_INSTANCE_STATE);
            setChecked(isChecked);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void initAttrs(AttributeSet attributeSet) {
        TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.SmoothCheckBox);
        tickColor = ta.getColor(R.styleable.SmoothCheckBox_color_tick, COLOR_TICK);
        mAnimDuration = ta.getInt(R.styleable.SmoothCheckBox_duration, DEF_ANIM_DURATION);
        mFloorUnCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_color_unchecked_stroke, COLOR_OUT_UNCHECKED);
        mCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_color_checked, COLOR_CHECKED);
        mUnCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_color_unchecked, COLOR_UNCHECKED);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.SmoothCheckBox_stroke_width, DensityUtils.dip2px(getContext(), 0));
        mChecked = ta.getBoolean(R.styleable.SmoothCheckBox_isCheck, false);
        ta.recycle();
    }

    @Override
    public void initValue() {
        mInPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickPaint.setStyle(Paint.Style.STROKE);
        mTickPaint.setStrokeCap(Paint.Cap.ROUND);
        mTickPaint.setColor(tickColor);


        mTickPath = new Path();
        mTickdstPath = new Path();

        resetValue();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    public void reset() {
        mCenterPoint = new Point(mCenterX, mCenterY);
        mStrokeWidth = (mStrokeWidth == 0 ? getMeasuredWidth() / 10 : mStrokeWidth);

        mTickPaint.setStrokeWidth(mStrokeWidth);

        mTickPath.reset();
        mTickdstPath.reset();
        tickStartPoint = new Point(Math.round(circleR / 15 * 7 + getPaddingLeft()), Math.round(circleR / 15 * 14) + getPaddingTop());
        mTickPath.moveTo(tickStartPoint.x, tickStartPoint.y);
        mTickPath.lineTo(Math.round(circleR / 15 * 13 + getPaddingLeft()), Math.round(circleR / 3 * 4) + getPaddingTop());
        mTickPath.lineTo(Math.round(circleR / 15 * 22 + getPaddingLeft()), Math.round(circleR / 3 * 2) + getPaddingTop());
        measure = new PathMeasure(mTickPath, false);         // 将 Path 与 PathMeasure  关联
        length = measure.getLength();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawOutCircle(canvas);
        drawInCircle(canvas);
        drawTick(canvas);
    }

    private void drawOutCircle(Canvas canvas) {
        mOutPaint.setColor(outColor);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, circleR * mOutScale, mOutPaint);
    }

    private void drawInCircle(Canvas canvas) {
        mInPaint.setColor(inColor);
        float radius = (circleR - mStrokeWidth) * mInScale;
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mInPaint);
    }

    private void drawTick(Canvas canvas) {
        if (!isChecked())
            return;

        mTickPaint.setColor(tickColor);
        mTickdstPath.reset();
        mTickdstPath.moveTo(tickStartPoint.x, tickStartPoint.y);
        mTickdstPath.lineTo(tickStartPoint.x, tickStartPoint.y);
        measure.getSegment(0, length * mTickScale, mTickdstPath, true);
        canvas.drawPath(mTickdstPath, mTickPaint);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        resetValue();
        invalidate();
        if (mListener != null) {
            mListener.onCheckedChanged(SmoothCheckBox.this, mChecked);
        }
    }

    private void resetValue() {
        mOutScale = isChecked() ? 1.0f : 0f;
        mInScale = isChecked() ? 0f : 1.0f;
        outColor = isChecked() ? mCheckedColor : mFloorUnCheckedColor;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }


    private void startCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0f);
        animator.setDuration(mAnimDuration / 3 * 2);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mInScale = (float) animation.getAnimatedValue();
                outColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mInScale);
                postInvalidate();
            }
        });
        animator.start();

        ValueAnimator floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f);
        floorAnimator.setDuration(mAnimDuration);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOutScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        floorAnimator.start();

//        drawTickDelayed();
    }

    private void startUnCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(mAnimDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mInScale = (float) animation.getAnimatedValue();
                outColor = getGradientColor(mCheckedColor, mFloorUnCheckedColor, mInScale);
                postInvalidate();
            }
        });
        animator.start();

        ValueAnimator floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f);
        floorAnimator.setDuration(mAnimDuration);
        floorAnimator.setInterpolator(new LinearInterpolator());
        floorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOutScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        floorAnimator.start();
    }

    private static int getGradientColor(int startColor, int endColor, float percent) {
        int startA = Color.alpha(startColor);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);

        int endA = Color.alpha(endColor);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        int currentA = (int) (startA * (1 - percent) + endA * percent);
        int currentR = (int) (startR * (1 - percent) + endR * percent);
        int currentG = (int) (startG * (1 - percent) + endG * percent);
        int currentB = (int) (startB * (1 - percent) + endB * percent);
        return Color.argb(currentA, currentR, currentG, currentB);
    }

    public void setOnCheckedChangeListener(SmoothCheckBox.OnCheckedChangeListener l) {
        this.mListener = l;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked);
    }
}
