package com.switcher;

import android.view.View;
import android.view.ViewPropertyAnimator;

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
                        strategy.intervalAndNext(3000);
                    }
                }).build();
    }

    public static SwitchStrategy makeCarouselStrategy() {
        return new SwitchStrategy.Builder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(2000);
                    }
                }).
                next(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, final SwitchStrategy strategy) {
                        View viewOut = switcher.getCurrentView();
                        viewOut.animate().setDuration(500).translationYBy(-switcher.getMeasuredHeight());

                        View viewIn = switcher.getNextView();
                        viewIn.setVisibility(View.VISIBLE);
                        viewIn.setY(switcher.getMeasuredHeight());
                        viewIn.animate().setDuration(500).translationYBy(-switcher.getMeasuredHeight()).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        strategy.intervalAndNext(2000);
                                    }
                                });

                        strategy.cancelInNeeded(viewOut.animate(), viewIn.animate());
                    }
                }).
                cancel(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        if (strategy.getCancelMembers() != null) {
                            for (Object obj:strategy.getCancelMembers()){
                                ((ViewPropertyAnimator) obj).cancel();
                            }
                        }
                    }
                }).build();
    }
}
