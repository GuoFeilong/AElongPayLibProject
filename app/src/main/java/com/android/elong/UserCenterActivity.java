package com.android.elong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by feilong.guo on 16/11/4.
 */

public class UserCenterActivity extends AppCompatActivity {
    private static final String TAG = "UserCenterActivity";
    private static final float MAX_FLOAT = 40.F;
    private LinearLayout userCenterContainer;
    private WaveLineCenterView waveLineCenterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        userCenterContainer = (LinearLayout) findViewById(R.id.ll_user_container);
        waveLineCenterView = (WaveLineCenterView) findViewById(R.id.wl_center);

        waveLineCenterView.setWaveHeightListener(new WaveLineCenterView.WaveHeightListener() {
            @Override
            public void currentWaveHeightScal(float currentWavePercent) {
                Log.e(TAG, "currentWaveHeightScal: ---->>currentWavePercent-->" + currentWavePercent);
                changeUserIconParm(MAX_FLOAT * (1 - currentWavePercent / 360F));
            }
        });
    }

    private void changeUserIconParm(float v) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin += v;
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        userCenterContainer.setLayoutParams(layoutParams);
    }
}
