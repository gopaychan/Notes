1. SELinux权限配置： 
    > log警告：E SELinux : avc:  denied  { find } for interface=vendor.display.color::IDisplayColor pid=1160 scontext=u:r:bootanim:s0 tcontext=u:object_r:hal_display_color_hwservice:s0 tclass=hwservice_manager permissive=0</br>
    > 配置结果：allow bootanim hal_display_color_hwservice:hwservice_manager { find };
