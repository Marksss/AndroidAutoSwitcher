English  |  [中文文档](README_cn.md)

# AndroidAutoSwitcher
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-12%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=12)
[![Download](https://jitpack.io/v/Marksss/AndroidAutoSwitcher.svg)](https://jitpack.io/#Marksss/AndroidAutoSwitcher)

AutoSwitchView is a view that can automatically switch between two children (items). Compared to ViewFlipper, it has better stability for reusing its children when working on large data sets. Compared to AdapterViewFlipper, its expansibility is more excellent.

![demo-gif](https://github.com/Marksss/AndroidAutoSwitcher/blob/master/gif/demo.gif)
## Usage
### Add the JitPack repository to your build file
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Add the dependency
```implementation 'com.github.Marksss:AndroidAutoSwitcher:v1.2'```
### Code in XML
```
    <com.switcher.AutoSwitchView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:switcher_repeatCount="1"
        app:switcher_autoStart="true"/>
```
If you want the switching animation to be infinite, you just need to set switcher_repeatCount -1.
### Code in Activity
```
AutoSwitchView as = (AutoSwitchView) findViewById(R.id.yourid);
as.setAdapter(new YourAdapter());
as.setSwitchStrategy(new YourStrategy()); // See Switching Strategy
as.startSwitcher(); // If you have set autoStart true, this is not needed.
```
### Switching Strategy
You can easily customize swtiching animations you like with SwitchStrategy. It supports both Animation and ObjectAnimator. Here are some builders of SwitchStrategy I have offered as follows.

 - AnimationStrategyBuilder:
 customize your own animation with Animation;
 - AnimatorStrategyBuilder: 
 customize your own animation with ObjectAnimator;
 - CarouselStrategyBuilder: 
 seamlessly switch between two items in different directions;
 - ContinuousStrategyBuilder: 
 switch between items smoothly without any pauses;
 
 An example:
 ```
 as.setSwitchStrategy(
      new AnimationStrategyBuilder(this, R.anim.anim_in, R.anim.anim_out).
          build()
);
 ```
 Another example:
 ```
 as.setSwitchStrategy(
      new CarouselStrategyBuilder().
          setAnimDuration(900).
          setInterpolator(new AccelerateDecelerateInterpolator()).
          setMode(DirectionMode.right2Left).
          build()
);
 ```

In most cases, strategies above are enough. If you want to customize animation that is not so complicated, you can use AnimationStrategyBuilde or AnimatorStrategyBuilder with your own Animation or ObjectAnimator.
### Additional
It usually is unnecessary. But if you want to customize more complicated animations, then you neeed to create your own SwitchStrategy through adding SingleOperator into BaseBuilder (init->next->withEnd) in turn to control all movements of the switcher.
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
## License
AutoSwitchView is released under the [Apache License Version 2.0](LICENSE).
