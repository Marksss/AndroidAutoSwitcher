package com.switcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

/**
 * Created by shenxl on 2018/7/11.
 */

public class AutoSwitchView extends FrameLayout {
    private static final int DEFUALT_ANIM_DURATION = 500;
    private static final int DEFAULT_INTERVAL = 3000;
    public static final int INFINITE = -1;

    private long mInterval = DEFAULT_INTERVAL;
    private long mAnimDuration = DEFUALT_ANIM_DURATION;
    private AbsBaseAdapter mAdapter;
    private ChildViewFactory mViewFactory = new ChildViewFactory();
    private OnItemClickListener mItemClickListener;
    private SwitchAnimStrategy mAnimStrategy;
    private boolean mWasRunningWhenDetached;
    private boolean mIsRunning;
    private boolean mAutoStart;
    private int mRepeatCount = INFINITE;
    private int mActionDownItemIndex = -1;

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
            if (ta.hasValue(R.styleable.AutoSwitchView_switcher_interval)) {
                setInterval(ta.getInt(R.styleable.AutoSwitchView_switcher_interval, DEFAULT_INTERVAL));
            }
            if (ta.hasValue(R.styleable.AutoSwitchView_switcher_animDuration)) {
                setAnimDuration(ta.getInt(R.styleable.AutoSwitchView_switcher_animDuration, DEFUALT_ANIM_DURATION));
            }
            ta.recycle();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mWasRunningWhenDetached = isRunning();
        cancelRolling();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mWasRunningWhenDetached) {
            postDelayed(mSwitchAnimRunnable, mInterval);
        } else if (mAutoStart) {
            start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (mItemClickListener != null && mAdapter != null && mAdapter.getItemCount() > 0) {
                    mActionDownItemIndex = mAdapter.getItemIndex();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mItemClickListener != null && mAdapter != null && mAdapter.getItemCount() > 0 && mAdapter.getItemIndex() == mActionDownItemIndex ){
                    mItemClickListener.onItemClick(this, mViewFactory.thisView(), mAdapter.getItemIndex());
                    return true;
                }
                performClick();
                mActionDownItemIndex = -1;
                break;
        }
        return false;
    }

    public boolean isRunning(){
        return mIsRunning;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
    }

    public long getInterval() {
        return mInterval;
    }

    public void setInterval(long interval) {
        mInterval = interval;
    }

    public long getAnimDuration() {
        return mAnimDuration;
    }

    public void setAnimDuration(long animDuration) {
        mAnimDuration = animDuration;
    }

    public void setAnimStrategy(SwitchAnimStrategy animStrategy) {
        mAnimStrategy = animStrategy;
    }

    public void setAdapter(AbsBaseAdapter adapter) {
        this.mAdapter = adapter;
        mViewFactory.setAdapter(adapter);
    }

    public void cancelRolling(){
        if (getChildCount() > 1){
            getChildAt(0).animate().cancel();
            getChildAt(1).animate().cancel();
        }
        removeCallbacks(mSwitchAnimRunnable);
        removeCallbacks(mAnimDelayRunnable);
        removeCallbacks(mStartDelayRunnable);
        mIsRunning = false;
    }

    public void start() {
        start(0);
    }

    public void start(long delayMillis) {
        cancelRolling();
        if (checkNoAnimCondtions()) {
            return;
        }

        if (getChildCount() == 0){
            addView(mViewFactory.thisView());
            addView(mViewFactory.nextView());
        }

        mAdapter.resetItemIndex();
        postDelayed(mStartDelayRunnable, delayMillis);
    }

    private void showIntervalState() {
        View childShow = mViewFactory.thisView();
        mViewFactory.updateViews(mViewFactory.thisView(), mAdapter.getItemIndex());
        childShow.setVisibility(VISIBLE);
        childShow.animate().cancel();

        mViewFactory.nextView().setVisibility(INVISIBLE);
    }

    private boolean checkNoAnimCondtions() {
        if (mAdapter == null || mAdapter.getItemCount() == 0){
            return true;
        } else if (mAdapter.getItemCount() == 1) {
            showIntervalState();
            return true;
        }
        return false;
    }

    private Runnable mSwitchAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAnimStrategy != null) {
                View viewOut = mViewFactory.thisView();
                mAnimStrategy.beforeAnimOut(AutoSwitchView.this, viewOut);
                mAnimStrategy.animOut(AutoSwitchView.this, viewOut,
                        viewOut.animate().setDuration(mAnimDuration)).start();

                View viewIn = mViewFactory.nextView();
                mViewFactory.updateViews(viewIn, mAdapter.nextItemIndex());
                viewIn.setVisibility(VISIBLE);
                mAnimStrategy.beforeAnimIn(AutoSwitchView.this, viewIn);
                mAnimStrategy.animIn(AutoSwitchView.this, viewIn,
                        viewIn.animate().setDuration(mAnimDuration)).start();
            }
            postDelayed(mAnimDelayRunnable, mAnimStrategy == null ? 0 : mAnimDuration);
        }
    };

    private Runnable mAnimDelayRunnable = new Runnable() {
        @Override
        public void run() {
            mActionDownItemIndex = -1;
            mAdapter.step();
            mViewFactory.step();
            if (mInterval > 0) {
                showIntervalState();
            }
            if (mRepeatCount == INFINITE || mAdapter.getRepeatedCount() < mRepeatCount) {
                postDelayed(mSwitchAnimRunnable, mInterval);
            } else {
                mIsRunning = false;
            }
        }
    };

    private Runnable mStartDelayRunnable = new Runnable() {
        @Override
        public void run() {
            mIsRunning = true;
            postDelayed(mSwitchAnimRunnable, mInterval);
            if (mInterval > 0) {
                showIntervalState();
            }
        }
    };

    public static abstract class AbsBaseAdapter<T extends AutoSwitchView.ViewHolder> {
        private int mItemIndex;
        private int mRepeatedCount;

        public abstract T onCreateView(Context context);
        public abstract void updateItem(T holder, int position);
        public abstract int getItemCount();

        private void step() {
            mItemIndex = nextItemIndex();
        }

        private int nextItemIndex() {
            int i = mItemIndex;
            i++;
            if (i >= getItemCount()){
                i = 0;
                mRepeatedCount++;
            }
            return i;
        }

        private void resetItemIndex(){
            mItemIndex = 0;
            mRepeatedCount = 0;
        }

        private int getItemIndex() {
            return mItemIndex;
        }

        public int getRepeatedCount() {
            return mRepeatedCount;
        }
    }

    private class ChildViewFactory {
        private ViewHolder[] mViewHolders = new ViewHolder[2];
        private int mViewIndex;
        private AbsBaseAdapter mAdapter;

        public void setAdapter(AbsBaseAdapter adapter) {
            mAdapter = adapter;
        }

        private View thisView() {
            return getView(mViewIndex);
        }

        private View nextView() {
            return getView(next(mViewIndex));
        }

        private View getView(int indext){
            if (indext >= mViewHolders.length) {
                return null;
            } else if (mViewHolders[indext] == null) {
                mViewHolders[indext] = mAdapter.onCreateView(getContext());
            }
            return mViewHolders[indext].getView();
        }

        private int next(int index) {
            index++;
            if (index >= mViewHolders.length){
                index = 0;
            }
            return index;
        }

        private void step(){
            mViewIndex = next(mViewIndex);
        }

        private void updateViews(View view, int index){
            if (view.getTag() != null) {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder.mItemIndex != index) {
                    holder.mItemIndex = index;
                    mAdapter.updateItem(holder, index);
                }
            }
        }
    }

    public static class ViewHolder{
        private View mView;
        private int mItemIndex = -1;

        public ViewHolder(View view) {
            mView = view;
            view.setTag(this);
        }

        public View getView() {
            return mView;
        }

        public Context getContext(){
            return mView.getContext();
        }
    }

    public interface OnItemClickListener{
        void onItemClick(AutoSwitchView parent, View child, int position);
    }

    public interface SwitchAnimStrategy{
        void beforeAnimOut(AutoSwitchView parent, View child);
        ViewPropertyAnimator animOut(AutoSwitchView parent, View child, ViewPropertyAnimator animator);
        void beforeAnimIn(AutoSwitchView parent, View child);
        ViewPropertyAnimator animIn(AutoSwitchView parent, View child, ViewPropertyAnimator animator);
    }
}