package com.switcher.base;

/**
 * Created by shenxl on 2018/7/21.
 */

public class Utils {
    public static int getIndexInLoop(int index, int start, int total){
        if (index >= start + total) {
            return start;
        } else if (index < start) {
            return total + start - 1;
        }
        return index;
    }
}
