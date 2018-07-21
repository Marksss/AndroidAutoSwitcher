package com.example.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.switcher.base.BaseSwitchView;

/**
 * Created by shenxl on 2018/7/21.
 */

public class SingleTextAdapter extends BaseSwitchView.AbsBaseAdapter {
    private String str;

    public SingleTextAdapter(String str) {
        this.str = str;
    }

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_single_text, null, false);
    }

    @Override
    public void updateItem(View view, int position) {
        TextView te = (TextView) view.findViewById(R.id.single_text);
        te.setText(str+" X"+position);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}