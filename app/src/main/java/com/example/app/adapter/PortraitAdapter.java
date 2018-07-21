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

public class PortraitAdapter extends BaseSwitchView.AbsBaseAdapter {

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_portrait, null, false);
    }

    @Override
    public void updateItem(View view, int position) {
        TextView name = (TextView) view.findViewById(R.id.item_name);
        TextView desc = (TextView) view.findViewById(R.id.item_desc);
        switch (position) {
            case 0:
                name.setText("Savannah");
                desc.setText("Life means progress and progress means suffering");
                break;
            case 1:
                name.setText("Alexandra");
                desc.setText("Human life is like flowing water");
                break;
            default:
                name.setText("Katherine");
                desc.setText("1111112222244433");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}