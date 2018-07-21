package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.switcher.AutoSwitchView;
import com.switcher.base.BaseSwitchView;
import com.switcher.builder.CarouselStrategyBuilder;
import com.switcher.builder.ContinuousStrategyBuilder;
import com.switcher.builder.DirectionMode;

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
        autoSwitchView.setOnItemClickListener(new BaseSwitchView.OnItemClickListener() {
            @Override
            public void onItemClick(BaseSwitchView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        });

        AutoSwitchView autoSwitchView1 = (AutoSwitchView) findViewById(R.id.auto_roll_1);
        autoSwitchView1.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView1.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(300).
                        setInterpolator(new OvershootInterpolator(0.8f)).
                        setMode(DirectionMode.bottom2Top).
                        build()
        );

        AutoSwitchView autoSwitchView2 = (AutoSwitchView) findViewById(R.id.auto_roll_2);
        autoSwitchView2.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView2.setSwitchStrategy(
                new CarouselStrategyBuilder().
                        setAnimDuration(900).
                        setInterpolator(new AccelerateDecelerateInterpolator()).
                        setMode(DirectionMode.left2Right).
                        build()
        );

        AutoSwitchView autoSwitchView3 = (AutoSwitchView) findViewById(R.id.auto_roll_3);
        autoSwitchView3.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView3.setSwitchStrategy(
                new ContinuousStrategyBuilder().
                        setDuration(500).
                        setMode(DirectionMode.top2Bottom).
                        build()
        );

        AutoSwitchView autoSwitchView4 = (AutoSwitchView) findViewById(R.id.auto_roll_4);
        autoSwitchView4.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView4.setSwitchStrategy(
                new ContinuousStrategyBuilder().
                        setDuration(2000).
                        setMode(DirectionMode.right2Left).
                        build()
        );
    }


    public class MyAdapter extends AutoSwitchView.AbsBaseAdapter {
        List<MyEntity> mEntityList;

        public MyAdapter(List<MyEntity> entityList) {
            mEntityList = entityList;
        }

        @Override
        public View makeView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.vertical_banner,null,false);
        }

        @Override
        public void updateItem(View view, int position) {
            MyEntity bean = mEntityList.get(position);
            TextView title = (TextView) view.findViewById(R.id.item_banner_text);
            TextView stones = (TextView) view.findViewById(R.id.item_banner_stones);
            title.setText(bean.title);
            stones.setText("X"+position);
        }

        @Override
        public int getItemCount() {
            return mEntityList == null ? 0 : mEntityList.size();
        }
    }

    public class MyEntity {
        public String title;

        public MyEntity(String title) {
            this.title = title;
        }
    }

}
