package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.autoroll.AutoRollView;
import com.example.app.vertical.VerticalRollAdapter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoRollView autoRollView = (AutoRollView) findViewById(R.id.auto_roll_0);
        autoRollView.setAdapter(new VerticalRollAdapter());
        autoRollView.startRolling();
        autoRollView.setOnItemClickListener(new AutoRollView.OnItemClickListener() {
            @Override
            public void onItemClick(AutoRollView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "position="+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
