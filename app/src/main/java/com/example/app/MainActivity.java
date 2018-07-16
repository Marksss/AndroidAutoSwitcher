package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.app.vertical.MyEntity;
import com.switcher.AutoSwitchView;
import com.switcher.strategy.VerticalRollStrategy;
import com.example.app.vertical.MyAdapter;

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
        autoSwitchView.startRolling();
        autoSwitchView.setOnItemClickListener(new AutoSwitchView.OnItemClickListener() {
            @Override
            public void onItemClick(AutoSwitchView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "position="+position, Toast.LENGTH_SHORT).show();
            }
        });

        AutoSwitchView autoSwitchView1 = (AutoSwitchView) findViewById(R.id.auto_roll_1);
        autoSwitchView1.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView1.setAnimStrategy(new VerticalRollStrategy(true));
        autoSwitchView1.startRolling();

        AutoSwitchView autoSwitchView2 = (AutoSwitchView) findViewById(R.id.auto_roll_2);
        autoSwitchView2.setAdapter(new MyAdapter(mEntityList));
        autoSwitchView2.setAnimStrategy(new VerticalRollStrategy(false));
        autoSwitchView2.startRolling();
    }
}
