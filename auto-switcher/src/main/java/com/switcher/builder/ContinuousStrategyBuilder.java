package com.switcher.builder;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;

/**
 * Created by shenxl on 2018/7/21.
 */

public class ContinuousStrategyBuilder {
    private long mDuration = 2000;
    private DirectionMode mMode = DirectionMode.right2Left;

    public ContinuousStrategyBuilder setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public ContinuousStrategyBuilder setMode(DirectionMode mode) {
        this.mMode = mode;
        return this;
    }

    public SwitchStrategy build() {
        return new SwitchStrategy.BaseBuilder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(final AutoSwitchView switcher, final SwitchStrategy strategy) {
                        final float end = switcher.getAdapter().getItemCount() + 1;
                        ValueAnimator animator1 = ValueAnimator.ofFloat(0f, end);
                        animator1.setDuration((long) (mDuration * end));
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
                                switch (mMode) {
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
                        switcher.getCurrentView().setX(0);
                        switcher.getCurrentView().setY(0);
                        switcher.getPreviousView().setX(0);
                        switcher.getPreviousView().setY(0);
                        if (strategy.getCancelMembers() != null) {
                            for (Object obj : strategy.getCancelMembers()) {
                                ((ValueAnimator) obj).cancel();
                            }
                        }
                    }
                }).build();
    }}
