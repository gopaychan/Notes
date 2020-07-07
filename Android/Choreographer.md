1. 构造方法会新建一个FrameDisplayEventReceiver对象mDisplayEventReceiver
    1. FrameDisplayEventReceiver的父类DisplayEventReceiver的构造方法会调用nativeInit方法初始化native层对应的Receiver（NativeDisplayEventReceiver），并调用其initialize方法（其基类DisplayEventDispatcher的方法）
        1. DisplayEventDispatcher的构造方法会构造一个native层的DisplayEventReceiver对象mReceiver。
            1. DisplayEventReceiver的构造方法会调用sf的createDisplayEventConnection方法（vsyncSource=0，调用到sf里面的mEventThread的createEventConnection方法）create一个EventThread::Connection对象mEventConnection；新建gui::BitTube对象mDataChannel并调用```mEventConnection->stealReceiveChannel```获取mEventConnection对象的mChannel（mEventConnection对象构造方法里面新建的gui::BitTube对象）的mReceiverFd。
        2. DisplayEventDispatcher::initialize：调用mReceiver的getFd获取上面说的mReceiverFd，并调用Looper对象的addFd监听mReceiverFd的EVENT_INPUT事件，如果sf端的send fd写入vsync信息的时候这个监听就会唤醒并回调DisplayEventDispatcher::handleEvent。
        （DisplayEventDispatcher派生自LooperCallback）
    2. DisplayEventDispatcher::handleEvent，最后会回调到NativeDisplayEventReceiver的dispatchVsync方法（dispatchVsync是DisplayEventDispatcher的一个纯虚方法，NativeDisplayEventReceiver派生自DisplayEventDispatcher）
    3. 最后通过env->CallVoidMethod，从jni层回到java层，回调到java层的DisplayEventReceiver的dispatchVsync方法，该方法又会调用onVsync方法，FrameDisplayEventReceiver重写了该方法。
    4. onVsync方法里面send了一个msg到当前Handler里面，并且是```setAsynchronous(true)```的，即在MessageQueue里面添加[屏障](消息处理机制.md#sync)之后只会执行的msg。等待该msg handler的时候会调用doFrame方法，这个方法里面会调用```doCallbacks(Choreographer.CALLBACK_TRAVERSAL, ...)```来回调ViewRootImpl的doTraversal方法。