package com.switcher.builder;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;
import com.switcher.base.ChainOperator;
import com.switcher.base.SingleOperator;

/**
 * Simple strategy to customize your own animation with {@link Animation}
 *
 * Created by shenxl on 2018/7/21.
 */

public class AnimationStrategyBuilder {
    private Animation mAnimationIn;
    private Animation mAnimationOut;
    private long mInterval = 3000;

    public AnimationStrategyBuilder(Animation animationIn, Animation animationOut) {
        mAnimationIn = animationIn;
        mAnimationOut = animationOut;
    }

    public AnimationStrategyBuilder(Context context, @AnimRes int resourceIDIn, @AnimRes int resourceIDOut){
        mAnimationIn = AnimationUtils.loadAnimation(context, resourceIDIn);
        mAnimationOut = AnimationUtils.loadAnimation(context, resourceIDOut);
    }

    public AnimationStrategyBuilder setInterval(long interval) {
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
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        if (mAnimationIn != null) {
                            switcher.getCurrentView().startAnimation(mAnimationIn);
                        }
                        if (mAnimationOut != null) {
                            switcher.getPreviousView().startAnimation(mAnimationOut);
                        }
                        operator.showNextWithInterval(mInterval);
                    }
                }).
                withEnd(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        if (mAnimationIn != null) {
                            mAnimationIn.cancel();
                        }
                        if (mAnimationOut != null) {
                            mAnimationOut.cancel();
                        }
                    }
                }).build();
    }
}
