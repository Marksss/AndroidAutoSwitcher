package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.example.app.adapter.BannerAdapter2;
import com.switcher.AutoSwitchView;
import com.switcher.builder.CarouselStrategyBuilder;
import com.switcher.builder.DirectionMode;

/**
 * Created by shenxl on 2018/7/21.
 */

public class StartActivity extends Activity {
    private AutoSwitchView mAswBanner;
    private EditText mContentEdt, mCountEdt;
    private BannerAdapter2 mBannerAdapter = new BannerAdapter2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mContentEdt = (EditText) findViewById(R.id.edt_content);
        mCountEdt = (EditText) findViewById(R.id.edt_count);
        mAswBanner = (AutoSwitchView) findViewById(R.id.start_banner);
        mAswBanner.setAdapter(mBannerAdapter);
        mAswBanner.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(900).
                        setInterpolator(new AccelerateDecelerateInterpolator()).
                        setMode(DirectionMode.right2Left).
                        build()
        );
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                try {
                    count = Integer.parseInt(mCountEdt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBannerAdapter.setCount(count);
                mBannerAdapter.setTitle(mContentEdt.getText().toString());
                mAswBanner.startSwitcher();
            }
        });

        mContentEdt.setText(mBannerAdapter.getTitle());
        mCountEdt.setText(mBannerAdapter.getCount()+"");

        findViewById(R.id.btn_example).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
            }
        });
    }
}
