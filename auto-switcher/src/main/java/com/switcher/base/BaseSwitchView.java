package com.switcher.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Base class for a {@link FrameLayout} container that can switch between views.
 *
 * Created by shenxl on 2018/7/19.
 */

public class BaseSwitchView extends FrameLayout {
    public static final int INFINITE = -1;

    protected int mWhichChild = 0;
    protected AbsBaseAdapter mAdapter;
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

    /**
     * @return Returns the index of the currently displayed child view.
     */
    public int getWhichChild() {
        return mWhichChild;
    }

    /**
     * Displays the data at the specified position on the current view and
     * other views will be hidden
     *
     * @param itemIndex
     */
    protected void setDisplayedItem(int itemIndex) {
        final int count = getChildCount();
        final View currentView = getCurrentView();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child != currentView) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
                if (mAdapter != null) {
                    updateView(child, itemIndex);
                    mAdapter.mWhichItem = itemIndex;
                }
            }
        }
    }

    /**
     * call before we need to update view immediately
     */
    public void clearTags(){
        for (int i = 0;i < getChildCount();i++){
            getChildAt(i).setTag(-1);
        }
    }

    public View getCurrentView(){
        return getChildAt(Utils.getIndexInLoop(mWhichChild, 0, getChildCount()));
    }

    public View getNextView(){
        return getChildAt(Utils.getIndexInLoop(mWhichChild + 1, 0, getChildCount()));
    }

    public View getPreviousView(){
        return getChildAt(Utils.getIndexInLoop(mWhichChild - 1, 0, getChildCount()));
    }

    public void stepForward() {
        mWhichChild = Utils.getIndexInLoop(mWhichChild + 1, 0, getChildCount());
    }

    public void stepBackward() {
        mWhichChild = Utils.getIndexInLoop(mWhichChild - 1, 0, getChildCount());
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

    /**
     * Displays the data at the current position on the current view
     */
    public void updateCurrentView(){
        if (mAdapter != null) {
            updateView(getCurrentView(), mAdapter.getCurrentIndex());
        }
    }

    /**
     * An Adapter object acts as a bridge between child views in {@link BaseSwitchView}
     * and the underlying data for that view.
     */
    public static abstract class AbsBaseAdapter {
        private int mWhichItem;

        /**
         * Create view that can be reused between items
         *
         * @param context
         * @return
         */
        public abstract View makeView(Context context);

        /**
         * Displays the data at the specified position on a specified view
         *
         * @param view
         * @param position
         */
        public abstract void updateItem(View view, int position);

        /**
         * @return How many items are in the data set represented by this Adapter.
         */
        public abstract int getItemCount();

        public final int getCurrentIndex() {
            return mWhichItem;
        }

        public final void setCurrentItem(int whichItem) {
            mWhichItem = whichItem;
        }

        public final int getNextIndex() {
            return Utils.getIndexInLoop(mWhichItem + 1, 0, getItemCount());
        }
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * View has been clicked.
     */
    public interface OnItemClickListener{
        void onItemClick(BaseSwitchView parent, View child, int position);
    }
}
