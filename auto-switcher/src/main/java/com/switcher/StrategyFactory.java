package com.switcher;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import static android.animation.ValueAnimator.INFINITE;

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

    public static SwitchStrategy makeContinuousStrategy(final long duration, final Mode mode) {
        return new SwitchStrategy.Builder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(final AutoSwitchView switcher, final SwitchStrategy strategy) {
                        final float end = switcher.getAdapter().getItemCount() + 1;
                        ValueAnimator animator1 = ValueAnimator.ofFloat(0f, end);
                        animator1.setDuration((long) (duration * end));
                        animator1.setRepeatCount(switcher.getRepeatCount());
                        animator1.setRepeatMode(ValueAnimator.RESTART);
                        animator1.setInterpolator(new LinearInterpolator());
                        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                double floor = Math.floor(value);
                                if (floor - 1 == switcher.getAdapter().getCurrentIndex()) {
                                    strategy.next();
                                }

                                float offset;
                                switch (mode) {
                                    case top2Bottom:
                                        offset = (float) ((value - floor) * switcher.getMeasuredHeight());
                                        switcher.getCurrentView().setY(floor == end - 1 ? -switcher.getMeasuredHeight() : offset - switcher.getMeasuredHeight());
                                        switcher.getPreviousView().setY(floor == 0 ? -switcher.getMeasuredHeight() : offset);
                                        break;
                                    case bottom2Top:
                                        offset = (float) ((value - floor) * switcher.getMeasuredHeight());
                                        switcher.getCurrentView().setY(floor == end - 1 ? switcher.getMeasuredHeight() : switcher.getMeasuredHeight() - offset);
                                        switcher.getPreviousView().setY(floor == 0 ? switcher.getMeasuredHeight() : -offset);
                                        break;
                                    case left2Right:
                                        offset = (float) ((value - floor) * switcher.getMeasuredWidth());
                                        switcher.getCurrentView().setX(floor == end - 1 ? -switcher.getMeasuredWidth() : offset - switcher.getMeasuredWidth());
                                        switcher.getPreviousView().setX(floor == 0 ? -switcher.getMeasuredWidth() : offset);
                                        break;
                                    case right2Left:
                                        offset = (float) ((value - floor) * switcher.getMeasuredWidth());
                                        switcher.getCurrentView().setX(floor == end - 1 ? switcher.getMeasuredWidth() : switcher.getMeasuredWidth() - offset);
                                        switcher.getPreviousView().setX(floor == 0 ? switcher.getMeasuredWidth() : -offset);
                                        break;
                                }
                            }
                        });
                        animator1.start();

                        strategy.cancelIfNeeded(animator1);
                    }
                }).
                cancel(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        if (strategy.getCancelMembers() != null) {
                            for (Object obj : strategy.getCancelMembers()) {
                                ((ValueAnimator) obj).cancel();
                            }
                        }
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
                        switch (mode) {
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
