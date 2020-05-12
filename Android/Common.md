1. 两个IPC重要的宏
    ```
    //frameworks/native/libs/binder/include/binder/IInterface.h(9.0)

    #define DECLARE_META_INTERFACE(INTERFACE)
    static const ::android::String16 descriptor;
    static ::android::sp<I##INTERFACE> asInterface(
            const ::android::sp<::android::IBinder>& obj);
    virtual const ::android::String16& getInterfaceDescriptor() const;
    I##INTERFACE();
    virtual ~I##INTERFACE();
    
    #define IMPLEMENT_META_INTERFACE(INTERFACE, NAME)
    const ::android::String16 I##INTERFACE::descriptor(NAME);
    const ::android::String16&
            I##INTERFACE::getInterfaceDescriptor() const {
        return I##INTERFACE::descriptor;
    }
    ::android::sp<I##INTERFACE> I##INTERFACE::asInterface(
            const ::android::sp<::android::IBinder>& obj)
    {
        ::android::sp<I##INTERFACE> intr;
        if (obj != NULL) {
            intr = static_cast<I##INTERFACE*>(
                obj->queryLocalInterface(
                        I##INTERFACE::descriptor).get());
            if (intr == NULL) {
                intr = new Bp##INTERFACE(obj);
            }
        }
        return intr;
    }
    I##INTERFACE::I##INTERFACE() { }
    I##INTERFACE::~I##INTERFACE() { }
    ```
2. IBinder转对应INTERFACE
    ```
    //frameworks/native/libs/binder/include/binder/IInterface.h(9.0)

    template<typename INTERFACE>
    inline sp<INTERFACE> interface_cast(const sp<IBinder>& obj)
    {
        return INTERFACE::asInterface(obj);
    }
    ```

3. Threads.cpp（system/core/libutils）:
    >- 调用其run方法会回调threadLoop

4. onFirstRef：
    >- RefBase（system/core/libutils）的一个方法，在ref第一次创建的时候会回调。

5. mk打印变量
    >- @echo "LOCAL_SRC_FILES: $(LOCAL_SRC_FILES)"

6. fork() 创建一个子进程，对于主进程fork返回新建子进程ID，子进程fork返回0。

7. pipe2(int pipefd[2], int flags) 创建一个管道用于进程间通信。pipefd[0] 是管道的读端，pipefd[1]是管道的写端。数据写入pipe的写端的时候被内核缓冲，直到被管道的读端读出。一般用于fork之后的父子进程之间的进程间通信。

8. socket:listen()会进入LISTEN状态；accept()如果client端调用connect的话，server端调用accept会得到一个client的socket，通过这个client的socket可以跟client通信（write，read）。

9. stdin,stdout,stderr这3个fp，是随着bin的开启默认打开的，其中0就是stdin，表示输入流，1代表stdout，2代表stderr（IptablesRestoreController里面用到）




settings put global DlnaPlayUrl "http://192.168.43.140:1678/%/sdcard/3月26日周四《梅花魂 》.mp4"

settings put global DlnaPlayUrl "http://192.168.43.140:1442/%25/sdcard/123.mp4"

