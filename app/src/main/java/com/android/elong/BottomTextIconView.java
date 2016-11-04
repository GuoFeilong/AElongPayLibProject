package com.android.elong;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by feilong.guo on 16/11/3.
 */

public class BottomTextIconView extends View {
    private static final int DEF_PADDING = 10;
    private Paint textPaint;
    private String textDesc;
    private Drawable textIcon;
    private Bitmap textIconBitmap;
    private int textBottomColor;
    private int textBottomSize;
    private int viewHeight;
    private Paint bitmapPaint;

    public BottomTextIconView(Context context) {
        this(context, null);
    }

    public BottomTextIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTextIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BottomTextIconView, defStyleAttr, R.style.def_bottom_text_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.BottomTextIconView_bottom_desc:
                    textDesc = typedArray.getString(attr);
                    break;
                case R.styleable.BottomTextIconView_bottom_text_color:
                    textBottomColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.BottomTextIconView_bottom_text_size:
                    textBottomSize = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.BottomTextIconView_bottom_icon:
                    textIcon = typedArray.getDrawable(attr);
                    break;
            }
        }
        typedArray.recycle();
        initDataAndTools();
    }

    private void initDataAndTools() {
        textPaint = new Paint();
        textPaint.setColor(textBottomColor);
        textPaint.setTextSize(textBottomSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint.setStrokeCap(Paint.Cap.ROUND);

        bitmapPaint = new Paint();
        bitmapPaint.setColor(Color.TRANSPARENT);
        textIconBitmap = drawable2Bitmap(textIcon);
    }


    private Bitmap drawable2Bitmap(Drawable textIcon) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) textIcon;
        return bitmapDrawable.getBitmap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(textIconBitmap, 0, viewHeight - textIconBitmap.getHeight(), textPaint);
        canvas.drawText(textDesc, textIconBitmap.getWidth() + DEF_PADDING, viewHeight, textPaint);
    }
}
