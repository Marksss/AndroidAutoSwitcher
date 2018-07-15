package com.example.app.vertical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.autoroll.AutoRollView;
import com.example.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shenxl on 2018/7/11.
 */

public class VerticalRollAdapter extends AutoRollView.AbsBannerAdapter<VerticalRollAdapter.MyViewHolder> {
    List<VerticalRollEntity> mEntityList;

    public VerticalRollAdapter() {
        mEntityList = Arrays.asList(
                new VerticalRollEntity("My Favourite Fruit is Apply"),
                new VerticalRollEntity("My Mother's Favourite Fruit is Blueberry"),
                new VerticalRollEntity("Anne's Favourite Fruit is Banana"),
                new VerticalRollEntity("Jake Hates Fruit"));
    }

    @Override
    public VerticalRollAdapter.MyViewHolder onCreateView(Context context) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.vertical_banner,null,false));
    }

    @Override
    public void updateItem(VerticalRollAdapter.MyViewHolder holder, int position) {
        VerticalRollEntity bean = mEntityList.get(position);
        holder.title.setText(bean.title);
        holder.stones.setText("X"+position);
    }

    @Override
    public int getItemCount() {
        return mEntityList == null ? 0 : mEntityList.size();
    }

    public class MyViewHolder extends AutoRollView.ViewHolder {
        TextView title;
        TextView stones;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_banner_text);
            stones = (TextView) view.findViewById(R.id.item_banner_stones);
        }
    }

}
