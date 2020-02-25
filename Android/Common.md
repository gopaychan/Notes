1. 两个IPC重要的宏
    ```
    //frameworks\base\include\utils\IInterface.h

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