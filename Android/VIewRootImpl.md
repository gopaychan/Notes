1. 由WindowManagerGlobal创建并保存在其mRoots里面。

2. ViewRootImpl的构造方法会调用WindowManagerGlobal.getWindowSession，获取一个IWindowSession对象mWindowSession，这是个单例，每个进程只有一个IWindowSession对象用于与WMS通信。

3. 在setView的时候会调用mWindowSession的addToDisplay方法传入W对象，跟WindowManagerService建立联系，WindowManagerService端会给其建立对应的WindowState对象，并保存W对象用于回调（包括触摸事件的传递）。

4. setView里面会调用enableHardwareAcceleration方法来判断是否打开硬件加速。

4. setView -> requestLayout -> scheduleTraversals ->mTraversalRunnable.run() -> doTraversal -> performTraversals -> (view的measure layout draw)