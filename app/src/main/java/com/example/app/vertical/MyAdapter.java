package com.example.app.vertical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.autoroll.AutoRollView;
import com.example.app.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shenxl on 2018/7/11.
 */

public class MyAdapter extends AutoRollView.AbsBannerAdapter<MyAdapter.MyViewHolder> {
    List<MyEntity> mEntityList;

    public MyAdapter() {
        mEntityList = Arrays.asList(
                new MyEntity("My Favourite Fruit is Apply"),
                new MyEntity("My Mother's Favourite Fruit is Blueberry"),
                new MyEntity("Anne's Favourite Fruit is Banana"),
                new MyEntity("Jake Hates Fruit"));
    }

    @Override
    public MyAdapter.MyViewHolder onCreateView(Context context) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.vertical_banner,null,false));
    }

    @Override
    public void updateItem(MyAdapter.MyViewHolder holder, int position) {
        MyEntity bean = mEntityList.get(position);
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
