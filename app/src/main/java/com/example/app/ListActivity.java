package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.switcher.AutoSwitchView;
import com.switcher.builder.CarouselStrategyBuilder;
import com.switcher.builder.DirectionMode;

/**
 * Created by shenxl on 2018/7/21.
 */

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 50;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                if (convertView == null) {
                    LayoutInflater inflater = ListActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.item_list, null);
                } else {
                    view = convertView;
                }
                TextView tv = (TextView) view.findViewById(R.id.textview);
                tv.setText("This is item "+position);
                AutoSwitchView autoSwitchView = (AutoSwitchView) view.findViewById(R.id.auto_roll_in_list);
                if (position % 3 == 0) {
                    autoSwitchView.setVisibility(View.VISIBLE);
                    autoSwitchView.setAdapter(new MyAdapter(position));
                    autoSwitchView.setSwitchStrategy(
                            new CarouselStrategyBuilder().
                                    setAnimDuration(900).
                                    setInterpolator(new AccelerateDecelerateInterpolator()).
                                    setMode(DirectionMode.right2Left).
                                    build()
                    );
                    autoSwitchView.startSwitcher();
                } else {
                    autoSwitchView.setVisibility(View.GONE);
                }

                return view;
            }
        });
    }

    public class MyAdapter extends AutoSwitchView.AbsBaseAdapter{
        private int count;

        public MyAdapter(int count) {
            this.count = count;
        }

        @Override
        public View makeView(Context context) {
            return getLayoutInflater().inflate(R.layout.item_in_list, null);
        }

        @Override
        public void updateItem(View view, int position) {
            TextView textView = (TextView) view.findViewById(R.id.text_item_in_list);
            textView.setText("i am rolling in a listview : "+position);
        }

        @Override
        public int getItemCount() {
            return count;
        }
    }
}
