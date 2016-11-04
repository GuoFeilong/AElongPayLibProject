package com.android.elong;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by user on 16/10/28.
 */

public class WaveLineCenterView extends View {
    private static final String TAG = "WaveLineCenterView";
    private static final int DEF_HEIGHT = 60;
    private static final float BOTTOM_HEIGHT_SCALE = 1 / 2.3F;
    private Paint waveFirstPaint;
    private Paint waveSecondPaint;
    private Paint waveBottomPaint;
    private float waveAmplifier;
    private float waveFrequency;
    private float wavePhase;
    private float wavePhaseSecond;
    private int waveLineWidth;
    private int viewHeight;
    private int viewWidth;
    private float viewCenterY;
    private int waveFirstColor;
    private int waveSecondColor;
    private int waveBottomColor;
    private RectF bottomReact;
    private WaveHeightListener waveHeightListener;

    public WaveLineCenterView(Context context) {
        this(context, null);
    }


    public WaveLineCenterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLineCenterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveLineView, defStyleAttr, R.style.def_waveline_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.WaveLineView_wave_first_color:
                    waveFirstColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.WaveLineView_wave_second_color:
                    waveSecondColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.WaveLineView_wave_line_width:
                    waveLineWidth = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.WaveLineView_wave_amplifier:
                    waveAmplifier = typedArray.getFloat(attr, 0);
                    break;
                case R.styleable.WaveLineView_wave_phase:
                    wavePhase = typedArray.getFloat(attr, 0);
                    break;
                case R.styleable.WaveLineView_wave_phase_second:
                    wavePhaseSecond = typedArray.getFloat(attr, 0);
                    break;
                case R.styleable.WaveLineView_wave_frequency:
                    waveFrequency = typedArray.getFloat(attr, 0);
                    break;
                case R.styleable.WaveLineView_wave_bottom_color:
                    waveBottomColor = typedArray.getColor(attr, Color.BLACK);
                    break;
            }
        }
        typedArray.recycle();
        initTools();
    }

    private void initTools() {
        waveFirstPaint = new Paint();
        waveFirstPaint.setAntiAlias(true);
        waveFirstPaint.setStyle(Paint.Style.FILL);
        waveFirstPaint.setStrokeJoin(Paint.Join.ROUND);
        waveFirstPaint.setStrokeCap(Paint.Cap.ROUND);
        waveFirstPaint.setColor(waveFirstColor);
        waveFirstPaint.setStrokeWidth(waveLineWidth);

        waveSecondPaint = new Paint();
        waveSecondPaint.setColor(waveSecondColor);
        waveSecondPaint.setStrokeCap(Paint.Cap.ROUND);
        waveSecondPaint.setStrokeJoin(Paint.Join.ROUND);
        waveSecondPaint.setStyle(Paint.Style.FILL);
        waveSecondPaint.setStrokeWidth(waveLineWidth);
        waveSecondPaint.setAntiAlias(true);

        waveBottomPaint = new Paint();
        waveBottomPaint.setColor(waveBottomColor);
        waveBottomPaint.setStyle(Paint.Style.FILL);

    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < viewWidth - 1; i++) {
            canvas.drawLine((float) i, viewCenterY - waveAmplifier * (float) (Math.sin(wavePhase * 2 * (float) Math.PI / 360.0f + 2 * Math.PI * waveFrequency * i / viewWidth)), (float) (i + 1), viewCenterY - waveAmplifier * (float) (Math.sin(wavePhase * 2 * (float) Math.PI / 360.0f + 2 * Math.PI * waveFrequency * (i + 1) / viewWidth)), waveFirstPaint);
            canvas.drawLine((float) i, viewCenterY - waveAmplifier * (float) (Math.sin(-wavePhaseSecond * 2 * (float) Math.PI / 360.0f + 2 * Math.PI * waveFrequency * i / viewWidth)), (float) (i + 1), viewCenterY - waveAmplifier * (float) (Math.sin(-wavePhaseSecond * 2 * (float) Math.PI / 360.0f + 2 * Math.PI * waveFrequency * (i + 1) / viewWidth)), waveSecondPaint);
        }
        canvas.drawRect(bottomReact, waveBottomPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        viewCenterY = viewHeight / 2;
        waveAmplifier = (waveAmplifier * 2 > viewHeight) ? (viewHeight / 2) : waveAmplifier;
        bottomReact = new RectF(0, viewHeight * BOTTOM_HEIGHT_SCALE, viewWidth, viewHeight);
        if (waveHeightListener != null) {
            waveAnim();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeaure;

        if (heightMeasureMode == MeasureSpec.AT_MOST || heightMeasureMode == MeasureSpec.UNSPECIFIED) {
            heightMeaure = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_HEIGHT, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeaure, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void waveAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.F, 0F);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float aFloat = Float.valueOf(animation.getAnimatedValue().toString());
                wavePhase = 360.F * aFloat;
                Log.e(TAG, "---wavePhase---" + wavePhase);
                if (waveHeightListener != null) {
                    waveHeightListener.currentWaveHeightScal(calculatePercent(wavePhase));
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private float calculatePercent(float wavePhase) {
        return wavePhase;
    }

    interface WaveHeightListener {
        void currentWaveHeightScal(float currentWavePercent);
    }

    public void setWaveHeightListener(WaveHeightListener waveHeightListener) {
        this.waveHeightListener = waveHeightListener;
    }
}
