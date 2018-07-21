package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.switcher.base.BaseSwitchView;

/**
 * Created by shenxl on 2018/7/21.
 */

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
                title.setText("This is title2");
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
