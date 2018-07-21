package com.switcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.switcher.base.BaseSwitchView;
import com.switcher.builder.DefaultStrategyBuilder;

/**
 * Created by shenxl on 2018/7/11.
 */

public class AutoSwitchView extends BaseSwitchView {
    public static final int INFINITE = -1;

    private SwitchStrategy mSwitchStrategy;
    private boolean mWasRunningWhenDetached;
    private boolean mIsRunning;
    private boolean mAutoStart;
    private int mHasRepeatedCount;
    private int mRepeatCount = INFINITE;

    public AutoSwitchView(Context context) {
        super(context);
        init(null);
    }

    public AutoSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AutoSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public AutoSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null){
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AutoSwitchView);
            if (ta.getBoolean(R.styleable.AutoSwitchView_switcher_autoStart, false)) {
                mAutoStart = true;
            }
            if (ta.hasValue(R.styleable.AutoSwitchView_switcher_repeatCount)) {
                setRepeatCount(ta.getInt(R.styleable.AutoSwitchView_switcher_repeatCount, INFINITE));
            }
            ta.recycle();
        }
        mSwitchStrategy = new DefaultStrategyBuilder().build();
    }

    @Override
    protected void onDetachedFromWindow() {
        mWasRunningWhenDetached = mIsRunning;
        mIsRunning = false;
        if (mSwitchStrategy != null) {
            mSwitchStrategy.stop();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mWasRunningWhenDetached) {
            mIsRunning = true;
            if (mSwitchStrategy != null) {
                mSwitchStrategy.restart();
            }
        } else if (mAutoStart) {
            startSwitcher();
        }
    }

    public void setSwitchStrategy(SwitchStrategy switchStrategy) {
        mSwitchStrategy = switchStrategy;
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
    }

    boolean needStop(){
        return checkNoAnimCondtions() || (mRepeatCount != INFINITE && mHasRepeatedCount >= mRepeatCount) ;
    }

    private boolean checkNoAnimCondtions() {
        if (getAdapter() == null || getAdapter().getItemCount() == 0){
            return true;
        } else if (getAdapter().getItemCount() == 1) {
            showIntervalState();
            return true;
        }
        return false;
    }

    public void startSwitcher(){
        mHasRepeatedCount = 0;
        if (checkNoAnimCondtions()){
            stopSwitcher();
            return;
        }

        if (getChildCount() == 0){
            addView(getAdapter().makeView(getContext()));
            addView(getAdapter().makeView(getContext()));
        }

        if (!mIsRunning) {
            if (mSwitchStrategy != null) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mSwitchStrategy.setSwitcher(AutoSwitchView.this);
                        mSwitchStrategy.init();
                    }
                });
            }
            mIsRunning = true;
        }
    }

    public void stopSwitcher(){
        mIsRunning = false;
        if (mSwitchStrategy != null) {
            mSwitchStrategy.stop();
        }
    }

    @Override
    public void stepOver() {
        super.stepOver();
        if (getAdapter().getCurrentIndex() == 0){
            mHasRepeatedCount++;
        }
    }
}