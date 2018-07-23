package com.switcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.switcher.base.BaseSwitchView;
import com.switcher.base.Utils;
import com.switcher.builder.DefaultStrategyBuilder;

/**
 * AutoSwitchView will animate between two views and one is shown at a time.
 * It can automatically switch between each child
 *
 * Created by shenxl on 2018/7/11.
 */

public class AutoSwitchView extends BaseSwitchView {
    public static final int INFINITE = -1;

    private SwitchStrategy mSwitchStrategy;
    private SwitchListener mSwitchListener;
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
        if (attrs != null) {
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
            mSwitchStrategy.onStop();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mWasRunningWhenDetached || mAutoStart) {
            startSwitcher();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        // Cancel animations or delays when it is hidden
        if (visibility != VISIBLE){
            stopSwitcher();
        }
        super.setVisibility(visibility);
    }

    @Override
    public void setDisplayedItem(int itemIndex) {
        // Cancel animations or delays when user choose one of items
        if (mIsRunning) {
            stopSwitcher();
        }
        super.setDisplayedItem(itemIndex);
    }

    /**
     * Customized animation strategy for switch between each child
     *
     * @param switchStrategy
     */
    public void setSwitchStrategy(SwitchStrategy switchStrategy) {
        if (mSwitchStrategy != null){
            mSwitchStrategy.onStop();
            mIsRunning = false;
        }
        mSwitchStrategy = switchStrategy;
    }

    public SwitchStrategy getSwitchStrategy() {
        return mSwitchStrategy;
    }

    public SwitchListener getSwitchListener() {
        return mSwitchListener;
    }

    public void setSwitchListener(SwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    /**
     * Sets how many times the animation should be repeated.
     * If the repeat count is 0, the animation is never repeated.
     * The repeat count is INFINITE by default.
     *
     * @param repeatCount the number of times the animation should be repeated
     * @attr ref android.R.styleable#switcher_repeatCount
     */
    public void setRepeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
    }

    /**
     * Start a timer to cycle through child views
     */
    public void startSwitcher() {
        mHasRepeatedCount = 0;
        if (checkNoAnimCondtions()) {
            stopSwitcher();
            return;
        }

        if (getChildCount() == 0) {
            addView(getAdapter().makeView(getContext()));
            addView(getAdapter().makeView(getContext()));
        }

        if (!mIsRunning) {
            if (mSwitchStrategy != null) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwitchListener != null) {
                            mSwitchListener.switchStart(AutoSwitchView.this);
                        }
                        mSwitchStrategy.setSwitcher(AutoSwitchView.this);
                        mSwitchStrategy.init();
                        mIsRunning = true;
                    }
                });
            }
        }
    }

    /**
     * Cancel animations or delays
     */
    public void stopSwitcher() {
        mIsRunning = false;
        if (mSwitchStrategy != null) {
            mSwitchStrategy.onStop();
        }
        resetIndex();
        if (mAdapter != null) {
            if (mAdapter.getItemCount() == 0) {
                removeAllViews();
            } else if (mAdapter.getItemCount() == 1){
                showIntervalState();
            }
        }
        if (mSwitchListener != null) {
            mSwitchListener.switchEnd(this);
        }
    }

    boolean needStop() {
        return checkNoAnimCondtions() || repeatOutOfLimit();
    }

    private boolean checkNoAnimCondtions() {
        return getAdapter() == null || getAdapter().getItemCount() < 2 || getVisibility() != VISIBLE;
    }

    private boolean repeatOutOfLimit(){
        return mRepeatCount != INFINITE && mHasRepeatedCount > mRepeatCount;
    }

    void showIntervalState() {
        //show one view for a moment (mInterval) betwwn two switching actions
        super.setDisplayedItem(mAdapter.getCurrentIndex());
    }

    void resetIndex() {
        mWhichChild = 0;
        if (mAdapter != null) {
            mAdapter.setCurrentItem(0);
        }
    }

    void stepOver() {
        // index of view ++
        mWhichChild = Utils.getIndexInLoop(mWhichChild + 1, 0, getChildCount());
        if (mAdapter != null) {
            // index of item ++
            mAdapter.setCurrentItem(Utils.getIndexInLoop(mAdapter.getCurrentIndex() + 1, 0, mAdapter.getItemCount()));
            if (mAdapter.getCurrentIndex() == 0) {
                mHasRepeatedCount++;
                if (mSwitchListener != null && !repeatOutOfLimit()) {
                    mSwitchListener.switchRepeat(this);
                }
            }
        }
    }

    /**
     * The animation listener to be notified when the animation of switcher starts, ends or repeats.
     */
    public interface SwitchListener {
        void switchStart(AutoSwitchView switcher);

        void switchRepeat(AutoSwitchView switcher);

        void switchEnd(AutoSwitchView switcher);
    }
}