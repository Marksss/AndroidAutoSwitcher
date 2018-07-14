package com.example.app;

import android.app.Activity;
import android.os.Bundle;

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
    }
}
