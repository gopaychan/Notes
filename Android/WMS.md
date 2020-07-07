##### 1. 其他
1. ![](../MdPicture/54.png)
1. ![](../MdPicture/38.png)
2. WindowState用来代表WMS中的窗口，而InputWindowHandle用来代表输入系统中的窗口。
3. WMS.mRoot里面的层级关系，最终所有的WindowState都保存在以mRoot为根的树里面。查找Focus也是遍历这棵树找到Focus的Window。
    1. WMS.mRoot(RootWindowContainer) > DisplayContent > TaskStackContainers(<font color=#00CED1>Contains all window containers that are related to apps (Activities)</font>) > TaskStack > Task > AppWindowToken > WindowState
    2. WMS.mRoot(RootWindowContainer) > DisplayContent > NonAppWindowContainers(<font color=#00CED1>Contains all non-app window containers that should be displayed below the app containers (e.g. Wallpaper).</font>) > WallpaperWindowToken > WindowState
    3. WMS.mRoot(RootWindowContainer) > DisplayContent > AboveAppWindowContainers(<font color=#00CED1>Contains all non-app window containers that should be displayed above the app containers (e.g. Status bar)</font>) > WindowToken > WindowState
    4. WMS.mRoot(RootWindowContainer) > DisplayContent > NonMagnifiableWindowContainers > WindowToken
4. WindowToken的构造方法会调用DisplayContent.reParentWindowToken -> DisplayContent.addWindowToken以ActivityRecord.Token为key把AppWindowToken保存在DisplayContent.mTokenMap里面。

##### 2. Window与WMS的联系
1. 每个App进程都会和WMS建立一个IWindowSession会话，这个会话被App进程用于和WMS通信。而WMS会保存每个ViewRootImpl.W（IWindow），这是个Binder对象，用来从WMS向client端Window的IPC调用。
2. WindowManagerImpl.addView最终调用到ViewRootImpl.setView，每一个Window都有与之联系的ViewRootView，不管是Activity还是Toast的Window。ViewRootImpl的setView会调用到Session（frameworks/base/service/core/java/com/android/server/wm目录下）的addToDisplay，最后调用到WMS的addWindow。客户端向WMS添加一个窗口的过程，其实就是WMS为其分配一块Surface的过程。
    > WMG.addView，主要是用来新建ViewRootImpl，并保存view（DecoderView），wparams和root（ViewRootImpl）到WMG对应的List里面，最后调用root.setView
    > ActivityThread.handleResumeActivity，Toast.handleShow都会调用addView。
3. WMS.addWindow，里面会新建Window在Service的一个代表WindowState，保存这传进来的IWindowSession和IWindow。
    > AMS里面说过Activity启动的时候在ActivityStack的startActivityLocked方法调用的过程中会新建AppWindowToken对象并保存在mRoot里面的某个结构里面，并以ActivityRecord.Token为key。在WMS.addView里面会通过ActivityRecord.Token来获取在ActivityRecord里面新建的[AppWindowToken](AMS.md#createWindowContainer)。

##### WindowManager
1. 常用Flag
    1. FLAG_NOT_FOCUSABLE：不许获得焦点。
    2. FLAG_NOT_TOUCHABLE：不接受触摸屏事件。
    3. FLAG_LAYOUT_NO_LIMITS：允许窗口延伸到屏幕外。
    4. FLAG_LAYOUT_IN_SCREEN：窗口占满整个屏幕，忽略周围的装饰边框（例如状态栏）。此窗口需要考虑到装饰边框的内容。
    5. FLAG_WATCH_OUTSIDE_TOUCH：如果设置了这个flag，当触屏事件发生在窗口之外时，可以通过设置此标志接收到一个 MotionEvent.ACTION_OUTSIDE事件。注意，你不会收到完整的down/move/up事件，只有第一次down事件时可以收到 ACTION_OUTSIDE。
    6. FLAG_NOT_TOUCH_MODAL：当窗口可以获得焦点（没有设置 FLAG_NOT_FOCUSALBE 选项）时，仍然将窗口范围之外的点击事件（鼠标、触摸屏）发送给后面的窗口处理。否则它将独占所有的点击事件，而不管它们是不是发生在窗口范围内。
    7. FLAG_SECURE：不允许屏幕截图。
    8. FLAG_HARDWARE_ACCELERATED：开启硬件加速。
2. WindowManager的关联类![](../MdPicture/49.png)

##### 深入理解Android卷三
1. WindowState的构造方法里面有窗口排序的逻辑，窗口的显示次序由两个成员字段描述：主序mBaseLayer和自序mSubLayer。主序用于描述窗口及其子窗口在所有窗口中的显示位置。而子序则描述一个子窗口在其兄弟窗口中的显示位置。主序越大，则窗口及其子窗口的显示位置相对于其他窗口的位置越靠前；子序越大同理。

2. WMS.relayoutWindow会[create](SurfaceFlinger.md#createsurface) Surface，Activity stop的时候会destroy Surface。