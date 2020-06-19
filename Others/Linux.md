1. 内核内存是进程共享的，所有应用程序进程公用同一份内核内存。[参考](https://blog.csdn.net/f22jay/article/details/7925531)

2. <span id="epoll">[epoll</span>](https://blog.csdn.net/weixin_34015336/article/details/85124052?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase)</span>[底层原理](https://zhuanlan.zhihu.com/p/87843750)
    1. epoll_wait是监听注册创建的epoll实例中的文件描述符（epoll_ctl EPOLL_CTL_ADD）的IO读写时间。如果这些文件描述符都没有发生IO读取事件，那么当前线程就会在函数epoll_wait中进入睡眠等待状态，等待时间由最后一个参数timeoutMillis来指定。
    2. epoll_create创建epoll对象
    3. epoll_ctl往epoll对象里面添加或删除某一个流的某一个事件。
        > ```
        > int mWakeEventFd;
        > mWakeEventFd = eventfd(0, EFD_NONBLOCK | EFD_CLOEXEC);
        >
        > struct epoll_event eventItem;
        > memset(& eventItem, 0, sizeof(epoll_event)); // zero out unused members of data field union
        > eventItem.events = EPOLLIN;
        > eventItem.data.fd = mWakeEventFd;
        > int result = epoll_ctl(mEpollFd, EPOLL_CTL_ADD, mWakeEventFd, & eventItem);
        > ```