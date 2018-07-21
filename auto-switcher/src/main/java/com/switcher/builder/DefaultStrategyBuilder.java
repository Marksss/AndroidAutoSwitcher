package com.switcher.builder;

import com.switcher.AutoSwitchView;
import com.switcher.SwitchStrategy;

/**
 * Created by shenxl on 2018/7/21.
 */

public class DefaultStrategyBuilder {
    private long mInterval;

    public DefaultStrategyBuilder setInterval(long interval) {
        mInterval = interval;
        return this;
    }

    public SwitchStrategy build() {
        return new SwitchStrategy.BaseBuilder().
                init(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(mInterval);
                    }
                }).
                next(new SwitchStrategy.SingleStep() {
                    @Override
                    public void operate(AutoSwitchView switcher, SwitchStrategy strategy) {
                        strategy.intervalAndNext(mInterval);
                    }
                }).build();
    }
}
