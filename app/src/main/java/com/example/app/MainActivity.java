package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.switcher.AutoSwitchView;
import com.switcher.strategy.CarouselStrategy;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    List<MyEntity> mEntityList = Arrays.asList(
            new MyEntity("My Favourite Fruit is Apply"),
                new MyEntity("My Mother's Favourite Fruit is Blueberry"),
                new MyEntity("Anne's Favourite Fruit is Banana"),
                new MyEntity("Jake Hates Fruit"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoSwitchView autoSwitchView = (AutoSwitchView) findViewById(R.id.auto_roll_0);
        autoSwitchView.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView.setOnItemClickListener(new AutoSwitchView.OnItemClickListener() {
            @Override
            public void onItemClick(AutoSwitchView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "position="+position, Toast.LENGTH_SHORT).show();
            }
        });

        AutoSwitchView autoSwitchView1 = (AutoSwitchView) findViewById(R.id.auto_roll_1);
        autoSwitchView1.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView1.setAnimStrategy(new CarouselStrategy().
                setInterpolator(new OvershootInterpolator(0.8f)));

        AutoSwitchView autoSwitchView2 = (AutoSwitchView) findViewById(R.id.auto_roll_2);
        autoSwitchView2.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView2.setAnimStrategy(new CarouselStrategy().
                setMode(CarouselStrategy.Mode.right2Left));

        AutoSwitchView autoSwitchView3 = (AutoSwitchView) findViewById(R.id.auto_roll_3);
        autoSwitchView3.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView3.setAnimStrategy(new CarouselStrategy().
                setMode(CarouselStrategy.Mode.left2Right).
                setInterpolator(new DecelerateInterpolator(1.5f)));

//        AutoSwitchView autoSwitchView4 = (AutoSwitchView) findViewById(R.id.auto_roll_4);
//        autoSwitchView4.setAdapter(new MyAdapter(mEntityList));
//        autoSwitchView4.setAnimStrategy(new CarouselStrategy().setMode(CarouselStrategy.Mode.right2Left));

    }
}
