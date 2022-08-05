##### 一. [UsbService](https://blog.csdn.net/u013928208/article/details/84586238)
> 包括了UserManager，UsbDeviceManager，UsbHostManager，UsbPortManager，UsbAlsaManager，UsbSettingsManager，UsbPermissionManager
1. UsbHostManager里面有一个名为“UsbService host thread”的线程，是在systemReady的时候启动的。里面调用了native方法monitorUsbHostBus，最终调用到usbhost.c里面，通过inotify机制监控/dev，/dev/bus，/dev/bus/usb等文件夹或文件夹里面文件的变化来获取usb设备add或remove的状态，最后回调到UsbHostManager的usbDeviceAdded和usbDeviceRemoved方法。ps：有线耳机的插拔监听就是通过这个。
    1. 如果插入的是音频设备（有线耳机）或MIDI设备，就会调用UsbAlsaManager的usbDeviceAdded方法，接着调用selectAlsaDevice->UsbAlsaDevice.start->UsbAlsaDevice.updateWiredDeviceConnectionState,最后调用到AudioService的setWiredDeviceConnectionState方法。
2. UsbPortManager：usb端口管理，其构造方法会获取Usb.cpp的一个Proxy，并调用其setCallback方法，在setCallback方法里面会新建一个线程调用work方法，循环监听uevent事件。如果监听到想要的uevent事件，就会回调给callback的notifyPortStatusChange，最后在UsbPortManager的updatePortsLocked方法里面处理usb设备的add or remove（handlePortAddedLocked，handlePortRemovedLocked）。
3. UsbDeviceManager：USB设备事务处理，控制adb，mtp，ptp等usb模式的切换和相应的通知提醒（通过设置特定的属性值触发init的一些action操作，切换usb状态）。通过UsbUEventObserver监听USB配置的变化，通过registerReceiver ACTION_USB_PORT_CHANGED ACTION_BATTERY_CHANGED ACTION_USB_DEVICE_ATTACHED ACTION_USB_DEVICE_DETACHED，监听usb端口的变化和插拔的变化。

##### 二. Vold
1. sdcard -> /storage/self/primary -> /mnt/user/0/primary -> /storage/emulated/0 -> noFuse ? /mnt/runtime/*/emulated/0 : ... -> data/media/0
    1. 其中mnt/runtime/*/emulated/0 -> data/media/0 是system/bin/sdcard里面挂载的，system/vold/model/EmulatedVolume.cpp里面调用。
    2. /storage/emulated/0是在zygote fork 进程的时候在com_android_internal_os_Zygote的MountEmulatedStorage方法里面根据获取到的mount_mode取值的不同，挂载不同路径到/storage。mount_mode的取值是ProcessList.startProcessLocked中通过getExternalStorageMountMode方法获得的。
        - mount_mode的取值会判断：OP_LEGACY_STORAGE（有没有声明requestLegacyExternalStorage=“true” 且 targetSdk<30，targetSdk<29默认为true，targetSdk>=30,不启用这个权限，这个权限是在apk安装成功后PermissionPolicyService.onPackageAdded决定的）; READ_EXTERNAL_STORAGE, OP_READ_EXTERNAL_STORAGE; WRITE_EXTERNAL_STORAGE, OP_WRITE_EXTERNAL_STORAGE
2. StorageManagerService的resetIfBootedAndConnected方法会reset vold 并重新挂载sdcard（data/media）
    1. reset 之后会调用onUserAdded和onUserStarted，onUserStarted里面会create /data/media/0的volume，并回调StorageManagerService的onVolumeCreate方法，onVolumeCreate方法里面又会调用vold的mount方法挂载/data/media/0。


##### 三. 其他
1. Android/data：除${userid}/Android/obb/${package}目录外，相同应用程序(相同包名)不同用户( ${userid} )的目录也是权限隔离的。但是${userid}/Android/obb/${package}是跨用户( ${userid} ) 共享的。

2. AudioService的setMode，如果一个应用设置了MODE_IN_COMMUNICATION，其他应用无法把它设置回MODE_NORMAL，但可以设置成其他，详情见setModeInt方法。

3. HIDL服务配置在（VintfObject.cpp文件里面由生命），可以看各个hidl服务用到的版本：
    1. /system/etc/vintf/manifest.xml
    2. /system/etc/vintf/manifest/*.xml if they exist
    3. /product/etc/vintf/manifest.xml if it exists
    4. /product/etc/vintf/manifest/*.xml if they exist
    5. (deprecated) /system/manifest.xml
