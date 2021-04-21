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
        > Linux用于安全机制的元素有好几种，用户，角色，类型，安全等级。这几种元素在某些场合，还是会被用到，但对于LSM和SE0Linux最重要的还是type。
    4.  SELinux全称Security-Enhanced Linux。SELinux在MAC上实现的，所以SELinux也是基于LSM标准。在Linux kernel 2.6后正式直接整合进Linux里面。
        > 1. MAC的安全策略文件的作用就是表明了允许干什么。学名是TEAC（Type Enforcement Access Control）。简称TE </br>
        > 2. 两种安全机制DAC(Discretionary Access Control)和MAC(Mandatory Access Control)。通俗地讲，这两个机制的区别是。在DAC里，如果一个应用获取了一个用户权限，如Root，那他的所有操作操作都是基于这个用户权限。而MAC就简单霸道好多，无论你是谁，甚至是有Root用户权限，文件权限为777，但每个动作都是需要被允许之后可以被执行。这里可以是在安全策略文件中被允许，与可以是用户手动允许。</br>
        > 3. Linux系统先做DAC检查。如果没有通过DAC权限检查，则操作直接失败。通过DAC检查之后，再做MAC权限检查。
    5. 根据SELinux规范，完整的SContext字符串为：user:role:type[:range]
    6. 根据SELinux规范，完整的allow相关的语句格式为：```rule_name source_type target_type : class perm_set```
        - perm_set语法比较奇特，前面有一个~号。它表示除了{entrypoint relabelto}之外，{chr_file #file}这两个object_class所拥有的其他操作
            > ```allow unconfineddomain {fs_type dev_type file_type}:{ chr_file file } ~{entrypoint relabelto}```; </br>
        - #特殊符号除了~外，还有-号和*号，其中：
            1. -号表示去除某项内容。
            2. *号表示所有内容。
        - 常见的Object class有: /system/sepolicy/private/security_classes文件里面通过class语句声明的。
        - 常见的perm_set有： /system/sepolicy/private/access_vectors文件里面通过```common common_name(如：file){permission_name ... }```语句声明的。另一种声明语句是：```class class_name [ inherits common_name ] { permission_name ... }```，通过class声明的（如 该文件中的dir）。
        > 栗子： allow netd proc:file write

            1. allow：TE的allow语句，表示授权。除了allow之外，还有allowaudit、dontaudit、neverallow等。
            2. netd：source type。也叫subject，domain。
            3. proc：target type。它代表其后的file所对应的Type。
            4. file：代表Object Class。它代表能够给subject操作的一类东西。例如File、Dir、socket等。在Android系统中，有一个其他Linux系统没有的Object Class，那就是Binder。
            5. write：在该类Object Class中所定义的操作。
    7. attribute关键字定义一个属性，type可以与一个或多个属性关联，如：```type ttyMT_device, dev_type;```，```attribute dev_type;```;关键字typeattribute，type有两个作用，定义（声明）并关联某个属性。可以把这两个作用分开，type定义，typeattribute进行关联如：```type httpd_user_content_t;``` ，```typeattribute httpd_user_content_t file_type, httpdcontent;```。
        