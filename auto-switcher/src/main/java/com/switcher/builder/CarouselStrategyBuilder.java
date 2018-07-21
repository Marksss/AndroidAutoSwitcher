package com.switcher.builder;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;

/**
 * Created by shenxl on 2018/7/21.
 */

public class CarouselStrategyBuilder {
    private long mInterval = 2000;
    private long mAnimDuration = 500;
    private DirectionMode mMode = DirectionMode.top2Bottom;
    private Interpolator mInterpolator;

    public CarouselStrategyBuilder setInterval(long interval) {
        this.mInterval = interval;
        return this;
    }

    public CarouselStrategyBuilder setAnimDuration(long animDuration) {
        this.mAnimDuration = animDuration;
        return this;
    }

    public CarouselStrategyBuilder setMode(DirectionMode mode) {
        this.mMode = mode;
        return this;
    }

    public CarouselStrategyBuilder setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    public SwitchStrategy build() {
        return new SwitchStrategy.BaseBuilder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(mInterval);
                    }
                }).
                next(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, final SwitchStrategy strategy) {
                        View viewOut = switcher.getPreviousView();
                        switch (mMode) {
                            case top2Bottom:
                                viewOut.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationYBy(switcher.getMeasuredHeight());
                                break;
                            case bottom2Top:
                                viewOut.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationYBy(-switcher.getMeasuredHeight());
                                break;
                            case left2Right:
                                viewOut.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationXBy(switcher.getMeasuredWidth());
                                break;
                            case right2Left:
                                viewOut.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationXBy(-switcher.getMeasuredWidth());
                                break;
                        }

                        View viewIn = switcher.getCurrentView();
                        viewIn.setVisibility(View.VISIBLE);
                        switch (mMode) {
                            case top2Bottom:
                                viewIn.setY(-switcher.getMeasuredHeight());
                                viewIn.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationYBy(switcher.getMeasuredHeight());
                                break;
                            case bottom2Top:
                                viewIn.setY(switcher.getMeasuredHeight());
                                viewIn.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationYBy(-switcher.getMeasuredHeight());
                                break;
                            case left2Right:
                                viewIn.setX(-switcher.getMeasuredWidth());
                                viewIn.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationXBy(switcher.getMeasuredWidth());
                                break;
                            case right2Left:
                                viewIn.setX(switcher.getMeasuredWidth());
                                viewIn.animate().setDuration(mAnimDuration).setInterpolator(mInterpolator).
                                        translationXBy(-switcher.getMeasuredWidth());
                                break;
                        }

                        viewIn.animate().withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                strategy.intervalAndNext(mInterval);
                            }
                        });
                        strategy.cancelIfNeeded(viewOut.animate(), viewIn.animate());
                    }
                }).
                cancel(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        switcher.getCurrentView().setX(0);
                        switcher.getCurrentView().setY(0);
                        switcher.getPreviousView().setX(0);
                        switcher.getPreviousView().setY(0);
                        if (strategy.getCancelMembers() != null) {
                            for (Object obj : strategy.getCancelMembers()) {
                                ((ViewPropertyAnimator) obj).cancel();
                            }
                        }
                    }
                }).build();
    }
}
