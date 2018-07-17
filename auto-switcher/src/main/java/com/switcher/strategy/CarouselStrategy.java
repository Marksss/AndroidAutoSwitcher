package com.switcher.strategy;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.switcher.AutoSwitchView;

/**
 * Created by shenxl on 2018/7/15.
 */

public class CarouselStrategy implements AutoSwitchView.SwitchAnimStrategy {
    private Mode mMode = Mode.top2Bottom;
    private Interpolator mInterpolator;

    public CarouselStrategy setMode(Mode mode) {
        mMode = mode;
        return this;
    }

    public CarouselStrategy setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    @Override
    public void beforeAnimOut(AutoSwitchView parent, View child) {

    }

    @Override
    public ViewPropertyAnimator animOut(AutoSwitchView parent, View child, ViewPropertyAnimator animator) {
        if (mInterpolator != null) {
            animator.setInterpolator(mInterpolator);
        }
        switch (mMode){
            case top2Bottom:
                return animator.translationYBy(parent.getMeasuredHeight());
            case bottom2Top:
                return animator.translationYBy(-parent.getMeasuredHeight());
            case left2Right:
                return animator.translationXBy(parent.getMeasuredWidth());
            case right2Left:
                return animator.translationXBy(-parent.getMeasuredWidth());
        }
        return animator;
    }

    @Override
    public void beforeAnimIn(AutoSwitchView parent, View child) {
        switch (mMode){
            case top2Bottom:
                child.setY(-parent.getMeasuredHeight());
                break;
            case bottom2Top:
                child.setY(parent.getMeasuredHeight());
                break;
            case left2Right:
                child.setX(-parent.getMeasuredWidth());
                break;
            case right2Left:
                child.setX(parent.getMeasuredWidth());
                break;
        }
    }

    @Override
    public ViewPropertyAnimator animIn(AutoSwitchView parent, View child, ViewPropertyAnimator animator) {
        if (mInterpolator != null) {
            animator.setInterpolator(mInterpolator);
        }
        switch (mMode){
            case top2Bottom:
                return animator.translationYBy(parent.getMeasuredHeight());
            case bottom2Top:
                return animator.translationYBy(-parent.getMeasuredHeight());
            case left2Right:
                return animator.translationXBy(parent.getMeasuredWidth());
            case right2Left:
                return animator.translationXBy(-parent.getMeasuredWidth());
        }
        return animator;
    }

    public enum Mode{
        top2Bottom, bottom2Top, left2Right, right2Left
    }
}
