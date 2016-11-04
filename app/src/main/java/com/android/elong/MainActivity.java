package com.android.elong;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ColorfulProgressBar colorfulProgressBar;
    private BottomTextIconView bottomTextIconView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ImageView imageView = (ImageView) findViewById(R.id.iv_round);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.splash);

//        int sizeW = PaymentUtil.dip2px(this, 220);
//        int sizeH = PaymentUtil.dip2px(this, 270);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizeW, sizeH, false);
//        imageView.setImageDrawable(new CustomRoundDrawable(scaledBitmap));

        final ClearCircleView clearCircleView = (ClearCircleView) findViewById(R.id.cc_wave);
        clearCircleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearCircleView.startWave();
            }
        }, 1000);
//        final ClearCircleView circleView = new ClearCircleView(this);
//        setContentView(circleView);
//        circleView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                circleView.startWave();
//            }
//        }, 1000);
        colorfulProgressBar = (ColorfulProgressBar) findViewById(R.id.cpb_progress);
        bottomTextIconView = (BottomTextIconView) findViewById(R.id.btv_test);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(bottomTextIconView, "scaleX", 1.F, 1.3F, 1F);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(bottomTextIconView, "scaleY", 1.F, 1.3F, 1F);

//        objectAnimatorY.setRepeatCount(ValueAnimator.INFINITE);
//        objectAnimatorX.setRepeatCount(ValueAnimator.INFINITE);

        animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
        animatorSet.setDuration(2000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(MainActivity.this, "该切换了....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, UserCenterActivity.class);
                startActivity(intent);
            }
        });

        animatorSet.start();

        Observable.interval(300, 300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        unsubscribe();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        int percent = aLong.intValue();
                        int m = percent >= 100 ? 100 : percent;
                        colorfulProgressBar.setPbPercent(m);
                        if (percent >= 100 * 300) {
                            onCompleted();
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
