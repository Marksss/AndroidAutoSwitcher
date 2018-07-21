package com.switcher;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by shenxl on 2018/7/19.
 */

public class SwitchStrategy {
    private boolean mIsStopped;
    private long mInterval;
    private AutoSwitchView mSwitcher;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Object[] mCancelMembers;

    private SingleStep mInitStep, mNextStep, mCancelStep;

    private SwitchStrategy(BaseBuilder builder) {
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
        mSwitcher.showIntervalState();
        if (mInitStep != null) {
            mInitStep.operate(mSwitcher, this);
        }
    }

    void stop(){
        mIsStopped = true;
        mHandler.removeCallbacksAndMessages(null);
        if (mCancelStep != null && mCancelMembers != null) {
            mCancelStep.operate(mSwitcher, this);
        }
    }

    public void next(){
        mSwitcher.stepOver();

        if (mIsStopped){
            return;
        } else if (mSwitcher.needStop()){
            mSwitcher.stopSwitcher();
            return;
        }

        mSwitcher.updateCurrentView();
        if (mNextStep != null) {
            mNextStep.operate(mSwitcher, this);
        }
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

    public void cancelIfNeeded(Object... ts){
        mCancelMembers = ts;
    }

    public Object[] getCancelMembers() {
        return mCancelMembers;
    }

    public interface SingleStep {
        void operate(AutoSwitchView switcher, SwitchStrategy strategy);
    }

    public static final class BaseBuilder {
        private SingleStep mInitStep;
        private SingleStep mNextStep;
        private SingleStep mCancelStep;

        public BaseBuilder() {
        }

        public BaseBuilder init(SingleStep val) {
            mInitStep = val;
            return this;
        }

        public BaseBuilder next(SingleStep val) {
            mNextStep = val;
            return this;
        }

        public BaseBuilder cancel(SingleStep val) {
            mCancelStep = val;
            return this;
        }

        public SwitchStrategy build() {
            return new SwitchStrategy(this);
        }
    }
}
