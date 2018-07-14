package com.autoroll;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shenxl on 2018/7/11.
 */

public class AutoRollView extends ViewGroup {
    // 动画时间
    private static final int ANIM_TIME = 500;
    // 停留时间
    private static final int RETENTION_TIME = 3000;
    // 周期
    private static final int TOTAL_TIME = ANIM_TIME + RETENTION_TIME;
    private AbsBannerAdapter mAdapter;
    private ChildViewContainer mChildContainer = new ChildViewContainer();
    private ValueAnimator mAnim;

    public AutoRollView(Context context) {
        super(context);
    }

    public AutoRollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoRollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), heightMeasureSpec);
            if (child.getMeasuredHeight() > maxHeight) {
                maxHeight = child.getMeasuredHeight();
            }
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDetachedFromWindow() {
        if (isAnimating()) {
            mAnim.cancel();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAnim != null && !mAnim.isStarted()) {
            startAnimator();
        }
    }

    public void setAdapter(AbsBannerAdapter adapter) {
        this.mAdapter = adapter;
        mChildContainer.setAdapter(adapter);
    }

    public boolean isAnimating(){
        return mAnim != null && mAnim.isStarted();
    }

    public void cancelAnimIfNeeded(){
        if (isAnimating()) {
            mAnim.cancel();
            mAnim = null;
        }
    }

    public void startAnim() {
        cancelAnimIfNeeded();
        if (checkSpecCondtion()) {
            return;
        }

        mAnim = ValueAnimator.ofFloat(0f, 1f);
        mAnim.setDuration(TOTAL_TIME);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (checkSpecCondtion()) {
                    animation.cancel();
                } else {
                    updateAnim((float) animation.getAnimatedValue());
                }
            }
        });

        mAdapter.resetItemIndex();
        startAnimator();
    }

    private void updateAnim(float animValue) {
        float limitValue = (float) TOTAL_TIME /(float) ANIM_TIME;
        float currentValue = animValue * limitValue;
        if (currentValue < limitValue - 1.0f) {
            // 停留阶段
            if (getChildCount() == 0){
                addView(mChildContainer.thisView(getContext()));
            } else if (getChildCount() > 1) {
                removeViewAt(0);
            }
            View child = getChildAt(0);
            mAdapter.addItemIndex();
            mChildContainer.updateViews(child, mAdapter.getItemIndex());
            mAdapter.lockItemIndex();
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        } else {
            // 切换动画阶段
            if (getChildCount() == 0){
                addView(mChildContainer.thisView(getContext()));
                addView(mChildContainer.nextView(getContext()));
            } else if (getChildCount() == 1){
                addView(mChildContainer.nextView(getContext()));
            }

            View childOut = getChildAt(0);
            View childIn = getChildAt(1);

            int borderPos = (int) (getMeasuredHeight() * (limitValue - currentValue));
            childOut.layout(0, borderPos - childOut.getMeasuredHeight(), childOut.getMeasuredWidth(), borderPos);
            childIn.layout(0, borderPos, childIn.getMeasuredWidth(), borderPos + childIn.getMeasuredHeight());

            mChildContainer.updateViews(childOut, mAdapter.getItemIndex());
            mChildContainer.updateViews(childIn, mAdapter.nextItemIndex());
            mAdapter.unlockItemIndex();
        }
    }

    private void startAnimator() {
        mAnim.start();
        mAdapter.lockItemIndex();
    }

    private boolean checkSpecCondtion() {
        if (mAdapter == null || mAdapter.getItemCount() == 0){
            removeAllViews();
            return true;
        } else if (mAdapter.getItemCount() == 1) {
            removeAllViews();
            View child = mChildContainer.thisView(getContext());
            addView(child);
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            mChildContainer.updateViews(child, 0);
            return true;
        }
        return false;
    }

    public static abstract class AbsBannerAdapter<T extends ViewHolder> {
        private int mItemIndex;
        private boolean mLocked;

        public abstract T onCreateView(Context context);
        public abstract void updateItem(T holder, int position);
        public abstract int getItemCount();

        private void lockItemIndex(){
            mLocked = true;
        }

        private void unlockItemIndex(){
            mLocked = false;
        }

        private void addItemIndex() {
            if (!mLocked) {
                mItemIndex = nextItemIndex();
            }
        }

        private int nextItemIndex() {
            int i = mItemIndex;
            i++;
            if (i >= getItemCount()){
                i = 0;
            }
            return i;
        }

        private void resetItemIndex(){
            mItemIndex = 0;
        }

        private int getItemIndex() {
            return mItemIndex;
        }
    }

    private class ChildViewContainer{
        private ViewHolder[] mViewHolders = new ViewHolder[2];
        private int mViewIndex;
        private AbsBannerAdapter mAdapter;

        public void setAdapter(AbsBannerAdapter adapter) {
            mAdapter = adapter;
        }

        private View thisView(Context context) {
            if (mViewHolders[mViewIndex] == null) {
                mViewHolders[mViewIndex] = mAdapter.onCreateView(context);
            }
            return mViewHolders[mViewIndex].getView();
        }

        private View nextView(Context context) {
            mViewIndex++;
            if (mViewIndex >= mViewHolders.length){
                mViewIndex = 0;
            }
            return thisView(context);
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
}