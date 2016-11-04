package com.android.elong;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by user on 16/10/24.
 */

public class CustomRoundDrawable extends Drawable {
    private Paint mPaint;
    private Bitmap mBitmap;
    private Path path;
    private RectF rectF;
    private int cornerSize;

    public CustomRoundDrawable(Bitmap mBitmap) {
        cornerSize = 20;
        this.mBitmap = mBitmap;
        BitmapShader bitmapShader = new BitmapShader(
                mBitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
        );
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);

//        creatRoundPath();

    }

    private void creatRoundPath() {
        path = new Path();
        path.moveTo(0, mBitmap.getWidth());
        path.lineTo(0, cornerSize);
        path.addArc(new RectF(0, 0, cornerSize, cornerSize), -90, 0);
        path.moveTo(cornerSize, 0);
        path.lineTo(mBitmap.getWidth() - cornerSize, 0);
        path.addArc(new RectF(mBitmap.getWidth() - cornerSize, 0, mBitmap.getWidth(), cornerSize), -90, 0);
        path.moveTo(mBitmap.getWidth(), cornerSize);
        path.lineTo(mBitmap.getWidth(), mBitmap.getHeight());
        path.close();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(rectF, cornerSize, cornerSize, mPaint);
//        canvas.drawPath(path, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }
}
