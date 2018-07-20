package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.switcher.AutoSwitchView;

import java.util.List;

/**
 * Created by shenxl on 2018/7/11.
 */

public class MyAdapter extends AutoSwitchView.AbsBaseAdapter {
    List<MyEntity> mEntityList;

    public MyAdapter(List<MyEntity> entityList) {
        mEntityList = entityList;
    }

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.vertical_banner,null,false);
    }

    @Override
    public void updateItem(View view, int position) {
        MyEntity bean = mEntityList.get(position);
        TextView title = (TextView) view.findViewById(R.id.item_banner_text);
        TextView stones = (TextView) view.findViewById(R.id.item_banner_stones);
        title.setText(bean.title);
        stones.setText("X"+position);
    }

    @Override
    public int getItemCount() {
        return mEntityList == null ? 0 : mEntityList.size();
    }
}
