package com.switcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by shenxl on 2018/7/19.
 */

public class BaseSwitchView extends FrameLayout {
    public static final int INFINITE = -1;

    private int mWhichChild = 0;
    private AbsBaseAdapter mAdapter;
    private OnItemClickListener mItemClickListener;
    private int mActionDownItemIndex = -1;

    public BaseSwitchView(Context context) {
        super(context);
    }

    public BaseSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        mWhichChild = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (mItemClickListener != null && mAdapter != null && mAdapter.getItemCount() > 0) {
                    mActionDownItemIndex = mAdapter.getCurrentIndex();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mItemClickListener != null && mAdapter != null && mAdapter.getItemCount() > 0 && mAdapter.getCurrentIndex() == mActionDownItemIndex ){
                    mItemClickListener.onItemClick(this, getCurrentView(), mAdapter.getCurrentIndex());
                    return true;
                }
                performClick();
                mActionDownItemIndex = -1;
                break;
        }
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setAdapter(AbsBaseAdapter adapter) {
        this.mAdapter = adapter;
    }

    public AbsBaseAdapter getAdapter() {
        return mAdapter;
    }

    public int getWhichChild() {
        return mWhichChild;
    }

    public void showIntervalState() {
        setDisplayedChild(mWhichChild);
    }

    public void setDisplayedChild(int childIndex) {
        final int count = getChildCount();
        final View currentView = getCurrentView();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child != currentView) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
                if (mAdapter != null) {
                    updateView(child, mAdapter.getCurrentIndex());
                }
            }
        }
    }

    public View getCurrentView(){
        return getChildAt(getIndexInLoop(mWhichChild, 0, getChildCount()));
    }

    public View getNextView(){
        return getChildAt(getIndexInLoop(mWhichChild + 1, 0, getChildCount()));
    }

    public View getPreviousView(){
        return getChildAt(getIndexInLoop(mWhichChild - 1, 0, getChildCount()));
    }

    public void resetIndex(){
        mWhichChild = 0;
        if (mAdapter != null) {
            mAdapter.mWhichItem = 0;
        }
    }

    public void stepOver() {
        mWhichChild = getIndexInLoop(mWhichChild + 1, 0, getChildCount());
        if (mAdapter != null) {
            mAdapter.mWhichItem = getIndexInLoop(mAdapter.mWhichItem + 1, 0, mAdapter.getItemCount());
        }
    }

    private void updateView(View view, int index){
        if (mAdapter != null && index < mAdapter.getItemCount()) {
            if (view.getTag() == null) {
                view.setTag(index);
                mAdapter.updateItem(view, index);
            } else {
                Integer i = (Integer) view.getTag();
                if (i != index) {
                    view.setTag(index);
                    mAdapter.updateItem(view, index);
                }
            }
        }
    }

    public void updateCurrentView(){
        if (mAdapter != null) {
            updateView(getCurrentView(), mAdapter.getCurrentIndex());
        }
    }

    public void updateNextView(){
        if (mAdapter != null) {
            updateView(getNextView(), mAdapter.getNextIndex());
        }
    }

    private static int getIndexInLoop(int index, int start, int total){
        if (index >= start + total) {
            return start;
        } else if (index < start) {
            return total + start - 1;
        }
        return index;
    }

    public static abstract class AbsBaseAdapter {
        private int mWhichItem;

        public abstract View makeView(Context context);
        public abstract void updateItem(View view, int position);
        public abstract int getItemCount();

        public int getCurrentIndex() {
            return mWhichItem;
        }

        public int getNextIndex() {
            return getIndexInLoop(mWhichItem + 1, 0, getItemCount());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(BaseSwitchView parent, View child, int position);
    }
}
