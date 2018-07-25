package com.switcher.base;

/**
 * Created by shenxl on 2018/7/23.
 */

public interface ChainOperator {
    /**
     * Displays the data next item on the next view and {@link SingleOperator}
     * that added in the next() in BaseBuilder will be invoked finally
     */
    void showNext();

    /**
     * Call #showNext() after some delay
     * @param delay
     */
    void showNextWithInterval(long delay);

    /**
     * Stop switching movements and animations
     */
    void onStop();

    /**
     * Cache objects that need to stopped
     *
     * @param ts
     */
    void stopWhenNeeded(Object... ts);

    /**
     * Get objects that cached in #stopWhenNeeded(Object...)
     *
     * @return
     */
    Object[] getStoppingMembers();
}
