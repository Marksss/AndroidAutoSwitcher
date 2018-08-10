[English](README.md)  |  中文文档

# AndroidAutoSwitcher
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-12%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=12)
[![Download](https://api.bintray.com/packages/markshawn/com.github.markshawn/auto-switcher/images/download.svg)](https://bintray.com/markshawn/com.github.markshawn/auto-switcher/_latestVersion)

AutoSwitchView是一个能自由切换两个子控件的控件容器，相比于ViewFlipper，它支持子控件的复用，数据量大的情况下能有更好的性能。同时，相比于AdapterViewFlipper，它有更好的扩展性，支持自定义各种切换动画。 

![demo-gif](https://github.com/Marksss/AndroidAutoSwitcher/blob/master/gif/demo.gif)
## 用法
### 将以下依赖添加到 `build.gradle` 文件中
```compile 'com.github.markshawn:auto-switcher:1.0.1'```
### XML中的代码
```
    <com.switcher.AutoSwitchView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:switcher_repeatCount="1"
        app:switcher_autoStart="true"/>
```
如果想让切换动画无限循环，只需将switcher_repeatCount设置为-1.
### Activity中的代码
```
AutoSwitchView as = (AutoSwitchView) findViewById(R.id.yourid);
as.setAdapter(new YourAdapter());
as.setSwitchStrategy(new YourStrategy()); // See Switching Strategy
as.startSwitcher(); // If you have set autoStart true, this is not needed.
```
### Switching Strategy
你能利用SwitchStrategy轻松定制你喜欢的切换动画，它同时支持Animation和 ObjectAnimator。以下是一些已经提供的动画策略的建造者类：

 - AnimationStrategyBuilde:
 支持利用Animation定制自己的动画;
 - AnimatorStrategyBuilder: 
 支持利用ObjectAnimator定制自己的动画;
 - CarouselStrategyBuilder: 
 支持在不同方向上的两个item的无缝切换;
 - ContinuousStrategyBuilder: 
 支持两个item之间的平滑无停顿的切换;
 
 一个栗子:
 ```
 as.setSwitchStrategy(
      new AnimationStrategyBuilder(this, R.anim.anim_in, R.anim.anim_out).
          build()
);
 ```
 另一个栗子:
 ```
 as.setSwitchStrategy(
      new CarouselStrategyBuilder().
          setAnimDuration(900).
          setInterpolator(new AccelerateDecelerateInterpolator()).
          setMode(DirectionMode.right2Left).
          build()
);
 ```

在大多数情况下，以上的切换动画策略已经足够。如果想定制不那么复杂的动画, 也可以将自己的Animation或者ObjectAnimator添加到AnimationStrategyBuilde或者AnimatorStrategyBuilder中产生对应的切换策略。
### 复杂定制（非必需）
如果想定制更加复杂的切换动画, 你可以轮流添加SingleOperator到BaseBuilder中，创建你自己的SwitchStrategy，这样就可以完全控制切换流程了（如下所示）。
```
new SwitchStrategy.BaseBuilder().
    init(new SingleOperator() {
        @Override
        public void operate(AutoSwitchView switcher, ChainOperator operator) {
            ...//init your animation
        }
    }).next(new SingleOperator() {
        @Override
        public void operate(AutoSwitchView switcher, ChainOperator operator) {
            ...//run your own animation
        }
    }).withEnd(new SingleOperator() {
        @Override
        public void operate(AutoSwitchView switcher, ChainOperator operator) {
            ...//cancle or end your animation
        }
    }).build();
```
## 许可证
AutoSwitchView基于 [Apache License Version 2.0](LICENSE) 发布。