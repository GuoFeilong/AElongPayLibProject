package com.android.elong;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16/10/28.
 */

public class ColorfulProgressBar extends View {
    private static final int UNIT_TIME = 100;
    private static final float PB_HEIGHT_SCALE = 1 / 3.F;
    private static final int DEF_PADDING = 10;
    private static final int DEF_HEIGHT = 60;

    private int viewWidth;
    private int viewHeight;
    private int progressHeight;
    private int pbRadius;
    private int cpBackgroundColor;
    private float currentPercent;
    private Paint cpBackgroundPaint;
    private RectF cpBackGroundRectF;
    private List<Integer> cpProgressColors;
    private List<RectF> cpProgressRectFs;
    private List<Paint> cpProgressPaints;


    public ColorfulProgressBar(Context context) {
        this(context, null);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorfulProgressBar, defStyleAttr, R.style.def_colorful_pb_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ColorfulProgressBar_cpb_background_color:
                    cpBackgroundColor = typedArray.getColor(attr, Color.BLACK);
                    break;
            }
        }
        typedArray.recycle();
        initDataAndTools();

    }

    private void initDataAndTools() {
        cpProgressColors = new ArrayList<>();
        cpProgressRectFs = new ArrayList<>();
        cpProgressPaints = new ArrayList<>();

        setPbColrs();
        cpBackgroundPaint = createPaint(cpBackgroundColor);
        for (int currentColor : cpProgressColors) {
            cpProgressPaints.add(createPaint(currentColor));
            cpProgressRectFs.add(new RectF());
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        progressHeight = (int) (PB_HEIGHT_SCALE * viewHeight);
        pbRadius = progressHeight / 2;
        cpBackGroundRectF = new RectF(DEF_PADDING, viewHeight / 2 - progressHeight / 2, viewWidth - DEF_PADDING, viewHeight / 2 + progressHeight / 2);

        Log.e("TAG", "调用onSizeChanged");
        progressAnim(currentPercent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPbBackground(canvas);
        drawPbProgress(canvas);
    }

    private void drawPbProgress(Canvas canvas) {
        try {
            for (int i = 0; i < cpProgressPaints.size(); i++) {
                canvas.drawRoundRect(cpProgressRectFs.get(i), pbRadius, pbRadius, cpProgressPaints.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("TAG", "drawPbProgress: " + e.toString());
        }
    }

    private void drawPbBackground(Canvas canvas) {
        canvas.drawRoundRect(cpBackGroundRectF, pbRadius, pbRadius, cpBackgroundPaint);
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(color);
        return paint;
    }

    private void setPbColrs() {
        cpProgressColors.add(ContextCompat.getColor(getContext(), R.color.color_gray_100));
        cpProgressColors.add(ContextCompat.getColor(getContext(), R.color.first_bg));
        cpProgressColors.add(ContextCompat.getColor(getContext(), R.color.second_bg));
        cpProgressColors.add(ContextCompat.getColor(getContext(), R.color.third_bg));
    }

    public void setPbPercent(float percent) {
        Log.e("TAG", "调用set方法");
        this.currentPercent = percent;
        progressAnim(percent);
    }

    private void progressAnim(float percent) {
        if ((int) percent == 100) {
            for (int i = 1; i < cpProgressRectFs.size(); i++) {
                cpProgressRectFs.get(i).set(DEF_PADDING, viewHeight / 2 - progressHeight / 2, 0, viewHeight / 2 + progressHeight / 2);
            }
            cpProgressRectFs.get(0).set(DEF_PADDING, viewHeight / 2 - progressHeight / 2, viewWidth - DEF_PADDING, viewHeight / 2 + progressHeight / 2);
            cpProgressRectFs.clear();
            return;
        }

        final float pbRight = (viewWidth - 2 * DEF_PADDING) * (percent / 100.F) + DEF_PADDING;
        RectF first = cpProgressRectFs.get(0);
        first.set(DEF_PADDING, viewHeight / 2 - progressHeight / 2, pbRight, viewHeight / 2 + progressHeight / 2);

        if (cpProgressRectFs.size() > 0) {
            for (int i = 1; i < cpProgressColors.size(); i++) {
                ValueAnimator valueAnimatorC = ValueAnimator.ofFloat(0F, 1F);
                final int finalI = i;
                valueAnimatorC.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float aFloat = Float.valueOf(animation.getAnimatedValue().toString());
                        if (cpProgressRectFs.size() > 0) {
                            RectF currentPb = cpProgressRectFs.get(finalI);
                            currentPb.set(DEF_PADDING, viewHeight / 2 - progressHeight / 2, pbRight * aFloat, viewHeight / 2 + progressHeight / 2);
                            invalidate();
                        }
                    }
                });
                valueAnimatorC.setRepeatCount(ValueAnimator.INFINITE);
                valueAnimatorC.setDuration(UNIT_TIME * cpProgressColors.size());
                valueAnimatorC.setStartDelay(i * UNIT_TIME);
                valueAnimatorC.start();
            }
        }
    }
}
