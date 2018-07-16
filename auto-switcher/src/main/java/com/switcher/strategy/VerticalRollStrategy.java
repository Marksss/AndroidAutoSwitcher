package com.switcher.strategy;

import android.view.View;
import android.view.ViewPropertyAnimator;

import com.switcher.AutoSwitchView;

/**
 * Created by shenxl on 2018/7/15.
 */

public class VerticalRollStrategy implements AutoSwitchView.SwitchAnimStrategy {
    private boolean mFromTop = true;

    public VerticalRollStrategy(boolean fromTop) {
        mFromTop = fromTop;
    }

    @Override
    public void beforeAnimOut(AutoSwitchView parent, View child) {

    }

    @Override
    public ViewPropertyAnimator animOut(AutoSwitchView parent, View child, ViewPropertyAnimator animator) {
        return animator.translationYBy(mFromTop ? parent.getMeasuredHeight() : -parent.getMeasuredHeight());
    }

    @Override
    public void beforeAnimIn(AutoSwitchView parent, View child) {
        child.setY(mFromTop ? -parent.getMeasuredHeight() : parent.getMeasuredHeight());
    }

    @Override
    public ViewPropertyAnimator animIn(AutoSwitchView parent, View child, ViewPropertyAnimator animator) {
        return animator.translationYBy(mFromTop ? parent.getMeasuredHeight() : -parent.getMeasuredHeight());
    }
}
