package com.example.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.switcher.base.BaseSwitchView;

/**
 * Created by shenxl on 2018/11/26.
 */
public class BannerAdapter2  extends BaseSwitchView.AbsBaseAdapter {
    private int count = 3;
    private String title = "This is banner";

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.start_banner_2, null, false);
    }

    @Override
    public void updateItem(View view, int position) {
        View container = view.findViewById(R.id.banner_container);
        ((TextView) view.findViewById(R.id.banner_title)).setText(title);
        ((TextView) view.findViewById(R.id.banner_pos)).setText("X"+position);
        switch (position % 3) {
            case 0:
                container.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                container.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimaryDark));
                break;
            default:
                container.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
                break;
        }

    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemCount() {
        return count;
    }
}