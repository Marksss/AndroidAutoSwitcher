package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.switcher.AutoSwitchView;
import com.switcher.base.BaseSwitchView;
import com.switcher.builder.CarouselStrategyBuilder;
import com.switcher.builder.ContinuousStrategyBuilder;
import com.switcher.builder.DirectionMode;

/**
 * Created by shenxl on 2018/7/21.
 */

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AutoSwitchView autoSwitchView2 = (AutoSwitchView) findViewById(R.id.auto_roll_start);
        autoSwitchView2.setAdapter(new StartAdapter());
        autoSwitchView2.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(900).
                        setInterpolator(new AccelerateDecelerateInterpolator()).
                        setMode(DirectionMode.right2Left).
                        build()
        );
        autoSwitchView2.setOnItemClickListener(new BaseSwitchView.OnItemClickListener() {
            @Override
            public void onItemClick(BaseSwitchView parent, View child, int position) {
                if (position == 0){
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                }
            }
        });
    }
}
