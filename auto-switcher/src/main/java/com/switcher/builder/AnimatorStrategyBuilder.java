package com.switcher.builder;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;
import com.switcher.base.ChainOperator;
import com.switcher.base.SingleOperator;

/**
 * Simple strategy to customize your own animation with {@link ObjectAnimator}
 *
 * Created by shenxl on 2018/7/21.
 */

public class AnimatorStrategyBuilder {
    private ObjectAnimator mAnimatorIn;
    private ObjectAnimator mAnimatorOut;
    private long mInterval = 3000;
    private Handler mHandler = new Handler();

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
                init(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        operator.showNextWithInterval(mInterval);
                    }
                }).
                next(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, final ChainOperator operator) {
                        long delay = 0;
                        if (mAnimatorIn != null) {
                            mAnimatorIn.setTarget(switcher.getCurrentView());
                            mAnimatorIn.start();
                            delay = mAnimatorIn.getDuration();
                        }
                        if (mAnimatorOut != null) {
                            mAnimatorOut.setTarget(switcher.getPreviousView());
                            mAnimatorOut.start();
                            delay = delay > mAnimatorOut.getDuration() ? delay : mAnimatorOut.getDuration();
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                operator.showNextWithInterval(mInterval);
                            }
                        }, delay);
                    }
                }).
                withEnd(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        if (mAnimatorIn != null) {
                            mAnimatorIn.cancel();
                        }
                        if (mAnimatorOut != null) {
                            mAnimatorOut.cancel();
                        }
                        mHandler.removeCallbacksAndMessages(null);
                    }
                }).build();
    }
}
