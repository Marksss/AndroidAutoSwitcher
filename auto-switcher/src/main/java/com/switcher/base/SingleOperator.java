package com.switcher.base;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;

/**
 * Created by shenxl on 2018/7/23.
 */

public interface SingleOperator {
    void operate(AutoSwitchView switcher, ChainOperator operator);
}
