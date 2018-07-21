package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.adapter.BannerAdapter;
import com.example.app.adapter.HornAdapter;
import com.example.app.adapter.PortraitAdapter;
import com.switcher.AutoSwitchView;
import com.switcher.base.BaseSwitchView;
import com.switcher.builder.CarouselStrategyBuilder;
import com.switcher.builder.ContinuousStrategyBuilder;
import com.switcher.builder.DirectionMode;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    List<String> mEntityList = Arrays.asList(
            "My Favourite Fruit is Apply",
            "My Mother's Favourite Fruit is Blueberry",
            "Anne's Favourite Fruit is Banana",
            "Jake Hates Fruit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoSwitchView aswBanner = (AutoSwitchView) findViewById(R.id.auto_roll_banner);
        aswBanner.setAdapter(new BannerAdapter());
        aswBanner.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(900).
                        setInterpolator(new AccelerateDecelerateInterpolator()).
                        setMode(DirectionMode.right2Left).
                        build()
        );
        aswBanner.setOnItemClickListener(new BaseSwitchView.OnItemClickListener() {
            @Override
            public void onItemClick(BaseSwitchView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        });

        AutoSwitchView autoSwitchView = (AutoSwitchView) findViewById(R.id.auto_roll_0);
        autoSwitchView.setAdapter(new HornAdapter(mEntityList));

        AutoSwitchView autoSwitchView1 = (AutoSwitchView) findViewById(R.id.auto_roll_1);
        autoSwitchView1.setAdapter(new HornAdapter(mEntityList));
        autoSwitchView1.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(500).
                        setInterpolator(new DecelerateInterpolator()).
                        setMode(DirectionMode.bottom2Top).
                        build()
        );

        AutoSwitchView autoSwitchView2 = (AutoSwitchView) findViewById(R.id.auto_roll_2);
        autoSwitchView2.setAdapter(new HornAdapter(mEntityList));
        autoSwitchView2.setSwitchStrategy(
                new ContinuousStrategyBuilder().
                        setDuration(500).
                        setMode(DirectionMode.top2Bottom).
                        build()
        );

        AutoSwitchView autoSwitchView3 = (AutoSwitchView) findViewById(R.id.auto_roll_3);
        autoSwitchView3.setAdapter(new PortraitAdapter());
        autoSwitchView3.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(900).
                        setInterpolator(new OvershootInterpolator(0.8f)).
                        setMode(DirectionMode.left2Right).
                        build()
        );

        AutoSwitchView autoSwitchView4 = (AutoSwitchView) findViewById(R.id.auto_roll_4);
        autoSwitchView4.setAdapter(new PortraitAdapter());
        autoSwitchView4.setSwitchStrategy(
                new ContinuousStrategyBuilder().
                        setDuration(2000).
                        setMode(DirectionMode.right2Left).
                        build()
        );
    }


}
