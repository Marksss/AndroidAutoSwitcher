# AndroidAutoSwitcher
AutoSwitchView is a view that can automatically switch between two children (items). It has high stability for reusing its children and excellent expansibility. 

![demo-gif](https://github.com/Marksss/AndroidAutoSwitcher/blob/master/gif/demo.gif)
## Usage
### Add the dependency to your project build.gradle file
```compile 'com.github.markshawn:auto-switcher:1.0.0'```
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
as.setSwitchStrategy(
       new CarouselStrategyBuilder().
            setAnimDuration(500).
            setInterpolator(new DecelerateInterpolator()).
            setMode(DirectionMode.bottom2Top).
            build()
        );
as.startSwitcher(); // If you have set autoStart true, this is not needed.
```
### Switching Strategy
You can easily customize swtiching animations you like through SwitchStrategy. It supports both Animation and ObjectAnimator. Here are some strategies I have offered as follows.

 - AnimationStrategyBuilde:
 customize your own animation with Animation;
 - AnimatorStrategyBuilder: 
 customize your own animation with ObjectAnimator;
 - CarouselStrategyBuilder: 
 switch between two items in different directions;
 - ContinuousStrategyBuilder: 
 switch between items smoothly without any pauses;

In most cases, strategies above are enough. If you want to customize animation that is not so complicated, you can use AnimationStrategyBuilde or AnimatorStrategyBuilder with your own Animation or ObjectAnimator.

------

(Here is optional)
But if you want to customize complicated animation, then you can create your own SwitchStrategy through adding SingleOperator into BaseBuilder (init->next->withend) in turn to control all movements of the switcher.
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
