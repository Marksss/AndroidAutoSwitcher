package com.switcher.builder;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.view.animation.AnimationUtils;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;

/**
 * Created by shenxl on 2018/7/21.
 */

public class AnimatorStrategyBuilder {
    private ObjectAnimator mAnimatorIn;
    private ObjectAnimator mAnimatorOut;
    private long mInterval = 3000;

    public AnimatorStrategyBuilder(ObjectAnimator animatorIn, ObjectAnimator animatorOut) {
        mAnimatorIn = animatorIn;
        mAnimatorOut = animatorOut;
    }

    public AnimatorStrategyBuilder(Context context, int resourceIDIn, int resourceIDOut) {
        mAnimatorIn = (ObjectAnimator) AnimatorInflater.loadAnimator(context, resourceIDIn);
        mAnimatorOut = (ObjectAnimator) AnimatorInflater.loadAnimator(context, resourceIDOut);
    }

    public AnimatorStrategyBuilder setInterval(long interval) {
        mInterval = interval;
        return this;
    }

    public SwitchStrategy build() {
        return new SwitchStrategy.BaseBuilder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.showNextAfterInterval(mInterval);
                    }
                }).
                next(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        if (mAnimatorIn != null) {
                            mAnimatorIn.setTarget(switcher.getCurrentView());
                            mAnimatorIn.start();
                        }
                        if (mAnimatorOut != null) {
                            mAnimatorOut.setTarget(switcher.getPreviousView());
                            mAnimatorOut.start();
                        }
                        strategy.showNextAfterInterval(mInterval);
                    }
                }).
                cancel(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        if (strategy.getCancelMembers() != null) {
                            for (Object obj : strategy.getCancelMembers()) {
                                ((ObjectAnimator) obj).cancel();
                            }
                        }
                    }
                }).build();
    }
}
