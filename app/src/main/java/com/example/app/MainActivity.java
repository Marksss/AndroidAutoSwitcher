package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.autoroll.AutoRollView;
import com.autoroll.strategy.VerticalRollStrategy;
import com.example.app.vertical.MyAdapter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoRollView autoRollView = (AutoRollView) findViewById(R.id.auto_roll_0);
        autoRollView.setAdapter(new MyAdapter());
        autoRollView.setAnimStrategy(null);
        autoRollView.startRolling();
        autoRollView.setOnItemClickListener(new AutoRollView.OnItemClickListener() {
            @Override
            public void onItemClick(AutoRollView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "position="+position, Toast.LENGTH_SHORT).show();
            }
        });

        AutoRollView autoRollView1 = (AutoRollView) findViewById(R.id.auto_roll_1);
        autoRollView1.setAdapter(new MyAdapter());
        autoRollView1.setAnimStrategy(new VerticalRollStrategy(true));
        autoRollView1.startRolling();

        AutoRollView autoRollView2 = (AutoRollView) findViewById(R.id.auto_roll_2);
        autoRollView2.setAdapter(new MyAdapter());
        autoRollView2.setAnimStrategy(new VerticalRollStrategy(false));
        autoRollView2.startRolling();
    }
}
