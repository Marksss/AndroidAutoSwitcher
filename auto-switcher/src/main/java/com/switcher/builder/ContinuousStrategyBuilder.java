package com.switcher.builder;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;
import com.switcher.base.ChainOperator;
import com.switcher.base.SingleOperator;

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
                init(new SingleOperator() {
                    @Override
                    public void operate(final AutoSwitchView switcher, final ChainOperator operator) {
                        final float end = switcher.getAdapter().getItemCount() + 1;
                        ValueAnimator animator = ValueAnimator.ofFloat(0f, end);
                        animator.setDuration((long) (mDuration * end));
                        animator.setRepeatCount(switcher.getRepeatCount());
                        animator.setRepeatMode(ValueAnimator.RESTART);
                        animator.setInterpolator(new LinearInterpolator());
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                double floor = Math.floor(value);
                                if (floor - 1 == switcher.getAdapter().getCurrentIndex()) {
                                    operator.showNext();
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
                        animator.start();

                        operator.stopWhenNeeded(animator);
                    }
                }).
                withEnd(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        switcher.getCurrentView().setX(0);
                        switcher.getCurrentView().setY(0);
                        switcher.getPreviousView().setX(0);
                        switcher.getPreviousView().setY(0);
                        if (operator.getStoppingMembers() != null) {
                            for (Object obj : operator.getStoppingMembers()) {
                                ((ValueAnimator) obj).cancel();
                            }
                        }
                    }
                }).build();
    }}
