package com.switcher;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.switcher.base.ChainOperator;
import com.switcher.base.SingleOperator;

/**
 * Simple {@link ViewAnimator} that will animate between two or more views
 * that have been added to it.  Only one child is shown at a time.  If
 * requested, can automatically flip between each child at a regular interval.
 *
 * Created by shenxl on 2018/7/19.
 */

public class SwitchStrategy implements ChainOperator {
    private boolean mIsStopped;
    private long mInterval;
    private AutoSwitchView mSwitcher;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Object[] mCancelMembers;

    private SingleOperator mInitStep, mNextStep, mStopStep;

    private SwitchStrategy(BaseBuilder builder) {
        mInitStep = builder.mInitStep;
        mNextStep = builder.mNextStep;
        mStopStep = builder.mStopStep;
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

    @Override
    public void onStop(){
        mIsStopped = true;
        mHandler.removeCallbacksAndMessages(null);
        if (mStopStep != null && mCancelMembers != null) {
            mStopStep.operate(mSwitcher, this);
        }
    }

    @Override
    public void showNext(){
        mSwitcher.stepOver();

        if (mIsStopped){
            return;
        } else if (mSwitcher.needStop()){
            mSwitcher.stopSwitcher();
            return;
        }

        mSwitcher.getCurrentView().setVisibility(View.VISIBLE);
        mSwitcher.updateCurrentView();
        if (mNextStep != null) {
            mNextStep.operate(mSwitcher, this);
        }
    }

    @Override
    public void showNextWithInterval(long delay){
        this.mInterval = delay;
        mSwitcher.showIntervalState();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showNext();
            }
        }, delay);
    }

    @Override
    public void stopWhenNeeded(Object... ts){
        mCancelMembers = ts;
    }

    @Override
    public Object[] getStoppingMembers() {
        return mCancelMembers;
    }

    public static final class BaseBuilder {
        private SingleOperator mInitStep;
        private SingleOperator mNextStep;
        private SingleOperator mStopStep;

        public BaseBuilder() {
        }

        public BaseBuilder init(SingleOperator val) {
            mInitStep = val;
            return this;
        }

        public BaseBuilder next(SingleOperator val) {
            mNextStep = val;
            return this;
        }

        public BaseBuilder withEnd(SingleOperator val) {
            mStopStep = val;
            return this;
        }

        public SwitchStrategy build() {
            return new SwitchStrategy(this);
        }
    }
}
