package com.switcher;

import android.os.Handler;

/**
 * Created by shenxl on 2018/7/19.
 */

public class SwitchStrategy {
    private boolean mIsStopped;
    private long mInterval;
    private AutoSwitchView mSwitcher;
    private Handler mHandler = new Handler();
    private Object[] mCancelMembers;

    private SingleStep mInitStep, mNextStep, mCancelStep;

    private SwitchStrategy(Builder builder) {
        mInitStep = builder.mInitStep;
        mNextStep = builder.mNextStep;
        mCancelStep = builder.mCancelStep;
    }

    void setSwitcher(AutoSwitchView switcher) {
        mSwitcher = switcher;
    }

    void init(){
        mIsStopped = false;
        mSwitcher.resetIndex();
        mSwitcher.updateCurrentView();
        if (mInitStep != null) {
            mInitStep.operate(mSwitcher, this);
        }
    }

    void restart(){
        mIsStopped = false;
        if (mInterval > 0){
            intervalAndNext(mInterval);
        } else {
            next();
        }
    }

    void stop(){
        mIsStopped = true;
        mHandler.removeCallbacksAndMessages(null);
        if (mCancelStep != null) {
            mCancelStep.operate(mSwitcher, this);
        }
    }

    public void next(){
        if (mIsStopped || mSwitcher.needStop()){
            return;
        }

        mSwitcher.updateNextView();
        if (mNextStep != null) {
            mNextStep.operate(mSwitcher, this);
        }
        mSwitcher.stepOver();
    }

    public void intervalAndNext(long delay){
        this.mInterval = delay;
        mSwitcher.showIntervalState();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, delay);
    }

    public void cancelInNeeded(Object... ts){
        mCancelMembers = ts;
    }

    public Object[] getCancelMembers() {
        return mCancelMembers;
    }

    public interface SingleStep {
        void operate(AutoSwitchView switcher, SwitchStrategy strategy);
    }

    public static final class Builder<T> {
        private SingleStep mInitStep;
        private SingleStep mNextStep;
        private SingleStep mCancelStep;

        public Builder() {
        }

        public Builder init(SingleStep val) {
            mInitStep = val;
            return this;
        }

        public Builder next(SingleStep val) {
            mNextStep = val;
            return this;
        }

        public Builder cancel(SingleStep val) {
            mCancelStep = val;
            return this;
        }

        public SwitchStrategy build() {
            return new SwitchStrategy(this);
        }
    }
}
