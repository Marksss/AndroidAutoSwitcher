package com.autoroll.strategy;

import android.view.View;
import android.view.ViewPropertyAnimator;

import com.autoroll.AutoRollView;

/**
 * Created by shenxl on 2018/7/15.
 */

public class VerticalRollStrategy implements AutoRollView.SwitchAnimStrategy {
    private boolean mFromTop = true;

    public VerticalRollStrategy(boolean fromTop) {
        mFromTop = fromTop;
    }

    @Override
    public void beforeAnimOut(AutoRollView parent, View child) {

    }

    @Override
    public ViewPropertyAnimator animOut(AutoRollView parent, View child, ViewPropertyAnimator animator) {
        return animator.translationYBy(mFromTop ? parent.getMeasuredHeight() : -parent.getMeasuredHeight());
    }

    @Override
    public void beforeAnimIn(AutoRollView parent, View child) {
        child.setY(mFromTop ? -parent.getMeasuredHeight() : parent.getMeasuredHeight());
    }

    @Override
    public ViewPropertyAnimator animIn(AutoRollView parent, View child, ViewPropertyAnimator animator) {
        return animator.translationYBy(mFromTop ? parent.getMeasuredHeight() : -parent.getMeasuredHeight());
    }
}
