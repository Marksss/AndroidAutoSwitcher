package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
                } else if (position == 1){
                    startActivity(new Intent(StartActivity.this, ListActivity.class));
                }
            }
        });
    }

    public class StartAdapter extends BaseSwitchView.AbsBaseAdapter {

        @Override
        public View makeView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.start_banner, null, false);
        }

        @Override
        public void updateItem(View view, int position) {
            View container = view.findViewById(R.id.banner_container);
            TextView title = (TextView) view.findViewById(R.id.banner_title);
            switch (position) {
                case 0:
                    container.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimary));
                    title.setText("More AutoSwitcher examples");
                    break;
                case 1:
                    container.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimaryDark));
                    title.setText("Test AutoSwitcher in a listview");
                    break;
                default:
                    container.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
                    title.setText("This is title3");
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
