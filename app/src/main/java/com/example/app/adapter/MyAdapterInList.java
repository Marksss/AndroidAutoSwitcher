package com.example.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.switcher.AutoSwitchView;

/**
 * Created by shenxl on 2018/7/21.
 */

public class MyAdapterInList extends AutoSwitchView.AbsBaseAdapter{
    private Activity mActivity;
    private int count;

    public MyAdapterInList(Activity activity, int count) {
        mActivity = activity;
        this.count = count;
    }

    @Override
    public View makeView(Context context) {
        return mActivity.getLayoutInflater().inflate(R.layout.item_in_list, null);
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