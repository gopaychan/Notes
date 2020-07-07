1. 由WindowManagerGlobal创建并保存在其mRoots里面。

2. ViewRootImpl的构造方法会调用WindowManagerGlobal.getWindowSession，获取一个IWindowSession对象mWindowSession，这是个单例，每个进程只有一个IWindowSession对象用于与WMS通信。

3. 在setView的时候会调用mWindowSession的addToDisplay方法传入W对象，跟WindowManagerService建立联系，WindowManagerService端会给其建立对应的WindowState对象，并保存W对象用于回调（包括触摸事件的传递）。

4. setView里面会调用enableHardwareAcceleration方法来判断是否打开硬件加速。

5. setView -> requestLayout -> scheduleTraversals ->mTraversalRunnable.run() -> doTraversal -> performTraversals -> (view的measure layout draw)
    1. scheduleTraversals
        1. 当当前没有Traversals Schedule的时候（！mTraversalScheduled）这个方法才会调用逻辑，并设置```mTraversalScheduled = true```
        2. 调用```mHandler.getLooper().getQueue().postSyncBarrier()```在Handler对应Looper（主线程Looper）里面的MessageQueue设置[同步屏障](消息处理机制.md#sync)。
        3. 调用Choreographer.postCallback方法实在Vsync信号到来的回调，回调最终会调用到doTraversal方法，这个方法会先移除同步屏障，在调用performTraversals，遍历View树进行Measure，Layout，Draw。

6. ViewRootlmpl身负了很多职责，主要有以下几点:
    1. View树的根并管理View树。
    2. 触发View的测量、布局和绘制。
    3. 输入事件的中转站。
    4. 管理Surface。
    5. 负责与WMS进行进程间通信。