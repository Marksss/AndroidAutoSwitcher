package com.switcher.base;

/**
 * Created by shenxl on 2018/7/23.
 */

public interface ChainOperator {
    void showNext();
    void showNextWithInterval(long delay);
    void onStop();
    void stopWhenNeeded(Object... ts);
    Object[] getStoppingMembers();
}
