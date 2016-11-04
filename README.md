# AElongPayLibProject
个人中心头像波动效果
###各种中心的头像随着波纹波动的效果如下

![这里写图片描述](http://img.blog.csdn.net/20161104191755472)


----------
这里只是简单的实现先用户头像波动,下面的设置条目自己填充


----------

####核心逻辑就是自定义波浪view透传出当前波浪的峰值即可


----------
**自定义波浪的核心代码和实现**

 1. 绘制正弦波,上篇博客有写过实现
      这个效果是根据上篇稍作修改[http://blog.csdn.net/givemeacondom/article/details/52937337](http://blog.csdn.net/givemeacondom/article/details/52937337)
 2. 填补波浪view下半部分的波浪造成的view留白
  

```
 @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        viewCenterY = viewHeight / 2;
        waveAmplifier = (waveAmplifier * 2 > viewHeight) ? (viewHeight / 2) : waveAmplifier;
        //增加的掩盖波浪留白代码,计算一个矩形范围位置位于view底部并且高度不大于波浪的最低峰值
        bottomReact = new RectF(0, viewHeight * BOTTOM_HEIGHT_SCALE, viewWidth, viewHeight);
        if (waveHeightListener != null) {
            waveAnim();
        }
    }
```
在绘制的onDraw中

```
 public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < viewWidth - 1; i++) {
            //绘制波浪..省略,详情看源代码下载,文章最后
        }
        //增加的绘制掩盖留白代码
        canvas.drawRect(bottomReact, waveBottomPaint);
    }
```

 3. 正弦波属性动画透传峰值;
      增加一个借口回调给UI拿到当前的相位
  

```
 interface WaveHeightListener {
        void currentWaveHeightScal(float currentWavePercent);
    }

    public void setWaveHeightListener(WaveHeightListener waveHeightListener) {
        this.waveHeightListener = waveHeightListener;
    }
```

在动画执行的时候透传出去

```
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float aFloat = Float.valueOf(animation.getAnimatedValue().toString());
                wavePhase = 360.F * aFloat;
                Log.e(TAG, "---wavePhase---" + wavePhase);
                if (waveHeightListener != null) {
                   //增加的透传峰值的代码 waveHeightListener.currentWaveHeightScal(calculatePercent(wavePhase));
                }
                invalidate();
            }
        });

```

 4. activity界面调用
 
		

```
        waveLineCenterView.setWaveHeightListener(new WaveLineCenterView.WaveHeightListener() {
            @Override
            public void currentWaveHeightScal(float currentWavePercent) {
                Log.e(TAG, "currentWaveHeightScal: ---->>currentWavePercent-->" + currentWavePercent);
                //UI拿到峰值,动态的改变用户头像的layoutparems
                changeUserIconParm(MAX_FLOAT * (1 - currentWavePercent / 360F));
            }
        });

```

----------

 5. 代码改变VIew属性老生常谈了就简单代码奉上
 

```
//这的入参v就是计算出来的变动值
      private void changeUserIconParm(float v) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin += v;
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        userCenterContainer.setLayoutParams(layoutParams);
    }
```


----------
#前方高能源代码下载地址:[https://github.com/GuoFeilong/AElongPayLibProject](https://github.com/GuoFeilong/AElongPayLibProject)


----------
#希望大家多多star,谢谢!
