1. SELinux权限配置： 
    > log警告：E SELinux : avc:  denied  { find } for interface=vendor.display.color::IDisplayColor pid=1160 scontext=u:r:bootanim:s0 tcontext=u:object_r:hal_display_color_hwservice:s0 tclass=hwservice_manager permissive=0</br>
    > 配置结果：allow bootanim hal_display_color_hwservice:hwservice_manager { find };
2. SELinux相关概念
    1. Security Context，安全上下文。Security Context的作用就是相当于这些文件和进程的“身份证”。
    2. SELinux Mode，SELinux有两种模块Permissve Mode（宽容模式）和Enforcing Mode（强制模式）。区别在于Permissive只会打印SELinux Log。而强制模式会进行真正拦截。如果拦截，kernel log中的关键字是"avc:denied"。
    3. u:object_r:system_file:s0（文件）、u:r:system_app:s0（进程）：
        1. U：一个名为U的SELinux用户
        2. object_r：这个标志位在进程里表示的是一个用户角色（role）但是文件无扮演任何角色，这仅是一个文件的标识
        3. system_file：这是一个type的标志位，也是TE里最重要的一个标示位。不然怎么称为TE（Type Enforcement）
        4. s0：LSM的级别
        > Linux用于安全机制的元素有好几种，用户，角色，类型，安全等级。这几种元素在某些场合，还是会被用到，但对于LSM和SELinux最重要的还是type。
