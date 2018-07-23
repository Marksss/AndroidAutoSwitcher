package com.switcher.builder;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;
import com.switcher.base.ChainOperator;
import com.switcher.base.SingleOperator;

/**
 * Created by shenxl on 2018/7/21.
 */

public class DefaultStrategyBuilder {
    private long mInterval = 3000;

    public DefaultStrategyBuilder setInterval(long interval) {
        mInterval = interval;
        return this;
    }

    public SwitchStrategy build() {
        return new SwitchStrategy.BaseBuilder().
                init(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        operator.showNextWithInterval(mInterval);
                    }
                }).
                next(new SingleOperator() {
                    @Override
                    public void operate(AutoSwitchView switcher, ChainOperator operator) {
                        operator.showNextWithInterval(mInterval);
                    }
                }).build();
    }
}
