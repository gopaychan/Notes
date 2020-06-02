1. jni
    1. 主要文件
        1. prebuilts/jdk/jak9/linux-x86/include/jni.h
        2. libcore/ojluni/src/main/native/jvm.h
        3. art/runtime/jni_internal.cc
    1. JavaVM:这个代表java的虚拟机。所有的工作都是从获取虚拟机的接口开始的。
        1. 第一种方式，在加载动态链接库的时候，JVM会调用JNI_OnLoad(JavaVM* jvm, void* reserved)（如果定义了该函数）。第一个参数会传入JavaVM指针。
        2. 第二种方式，在native code中调用JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args)可以得到JavaVM指针。
            两种情况下，都可以用全局变量，比如JavaVM* g_jvm来保存获得的指针以便在任意上下文中使用。
        3. Android系统是利用第二种方式Invocation interface来创建JVM的。
    2. JNIEnv:JNI Interface Pointer, 是提供JNI Native函数的基础环境，线程相关，不同线程的JNIEnv相互独立。
    3. [Android官方一些解读](https://developer.android.com/training/articles/perf-jni.html#faq_FindClass)
    4. 获取JNIEnv
        ```
        JNIEnv* getEnv() {
            JNIEnv *env;
            int status = gJvm->GetEnv((void**)&env, 
            JNI_VERSION_1_6);
            if(status < 0) {
                status = gJvm->AttachCurrentThread(&env, NULL);
                if(status < 0) {
                    return nullptr;
                }
            }
            return env;
        }
        ```
    5. 注册方法
        ```
        static const JNINativeMethod gMethods[] = {
            { "class_init_native","()V", (void *)jni_class_init_native },
            { "native_input_pen_init","()Z", (void *)jni_input_pen_init },
            { "native_input_pen_exit","()V", (void *)jni_input_pen_exit },
        };

        static int registerMethods(JNIEnv* env) {
            const char* const kClassName = CLASS_NAME;
            jclass clazz;
            /* look up the class */
            clazz = env->FindClass(kClassName);
            if (clazz == NULL) {
                LOGE("Can't find class %s/n", kClassName);
                return -1;
            }
            /* register all the methods */
            if (env->RegisterNatives(clazz,gMethods,sizeof(gMethods)/sizeof(gMethods[0])) != JNI_OK) {
                LOGE("Failed registering methods for %s/n", kClassName);
                return -1;
            }
            /* fill out the rest of the ID cache */
            return 0;
        }

        jniRegisterNativeMethods方法也是走了上面的流程。
        ```

2. enum
    1. Java 中的每一个枚举都继承自 java.lang.Enum 类。当定义一个枚举类型时，每一个枚举类型成员都可以看作是 Enum 类的实例，这些枚举成员默认都被 final、public, static 修饰，当使用枚举类型成员时，直接使用枚举名称调用成员即可。

3. 找机会阅读java编程思想，java虚拟机，Linux相关的东西