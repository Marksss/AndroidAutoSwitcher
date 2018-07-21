package com.example.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.switcher.AutoSwitchView;

import java.util.List;

/**
 * Created by shenxl on 2018/7/21.
 */


public class HornAdapter extends AutoSwitchView.AbsBaseAdapter {
    List<String> mEntityList;

    public HornAdapter(List<String> entityList) {
        mEntityList = entityList;
    }

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.vertical_banner,null,false);
    }

    @Override
    public void updateItem(View view, int position) {
        String bean = mEntityList.get(position);
        TextView title = (TextView) view.findViewById(R.id.item_banner_text);
        TextView stones = (TextView) view.findViewById(R.id.item_banner_stones);
        title.setText(bean);
        stones.setText("X"+position);
    }

    @Override
    public int getItemCount() {
        return mEntityList == null ? 0 : mEntityList.size();
    }
}