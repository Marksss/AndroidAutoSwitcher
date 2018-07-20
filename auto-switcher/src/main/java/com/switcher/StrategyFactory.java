package com.switcher;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Created by shenxl on 2018/7/19.
 */

public class StrategyFactory {
    public static SwitchStrategy makeDefaultStrategy() {
        return new SwitchStrategy.Builder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(3000);
                    }
                }).
                next(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(2000);
                    }
                }).build();
    }

    public static SwitchStrategy makeCarouselStrategy(final long interval, final long animDuration, final Mode mode, final Interpolator interpolator) {
        return new SwitchStrategy.Builder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(interval);
                    }
                }).
                next(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, final SwitchStrategy strategy) {
                        View viewOut = switcher.getPreviousView();
                        switch (mode) {
                            case top2Bottom:
                                viewOut.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationYBy(switcher.getMeasuredHeight());
                                break;
                            case bottom2Top:
                                viewOut.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationYBy(-switcher.getMeasuredHeight());
                                break;
                            case left2Right:
                                viewOut.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationXBy(switcher.getMeasuredWidth());
                                break;
                            case right2Left:
                                viewOut.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationXBy(-switcher.getMeasuredWidth());
                                break;
                        }

                        View viewIn = switcher.getCurrentView();
                        viewIn.setVisibility(View.VISIBLE);
                        switch (mode){
                            case top2Bottom:
                                viewIn.setY(-switcher.getMeasuredHeight());
                                viewIn.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationYBy(switcher.getMeasuredHeight());
                                break;
                            case bottom2Top:
                                viewIn.setY(switcher.getMeasuredHeight());                                
                                viewIn.animate().setDuration(animDuration).setInterpolator(interpolator).
                                    translationYBy(-switcher.getMeasuredHeight());
                                break;
                            case left2Right:
                                viewIn.setX(-switcher.getMeasuredWidth());
                                viewIn.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationXBy(switcher.getMeasuredWidth());
                                break;
                            case right2Left:
                                viewIn.setX(switcher.getMeasuredWidth());
                                viewIn.animate().setDuration(animDuration).setInterpolator(interpolator).
                                        translationXBy(-switcher.getMeasuredWidth());
                                break;
                        }

                        viewIn.animate().withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                strategy.intervalAndNext(interval);
                            }
                        });
                        strategy.cancelIfNeeded(viewOut.animate(), viewIn.animate());
                    }
                }).
                cancel(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        if (strategy.getCancelMembers() != null) {
                            for (Object obj : strategy.getCancelMembers()) {
                                ((ViewPropertyAnimator) obj).cancel();
                            }
                        }
                    }
                }).build();
    }


    public enum Mode {
        top2Bottom, bottom2Top, left2Right, right2Left
    }
}
