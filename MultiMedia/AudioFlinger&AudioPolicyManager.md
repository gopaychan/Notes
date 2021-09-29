1. AudioFlinger主要控制播放过程，包括保存播放设备;AudioPolicyManager主要是一些策略相关的控制。
2. AudioFlinger的构造方法会```mDevicesFactoryHal = DevicesFactoryHalInterface::create();```构造一个Device工厂。
    1. mDevicesFactoryHal最终是指向一个DevicesFactoryHalHybrid（mHidlFactory里面的mHidlFactory-IDevicesFactory对象为hardware/interfaces/audio/core/all-versions/default/DevicesFactory.cpp）对象。
3. AudioPolicyService的onFirstRef()方法会调用createAudioPolicyManager()方法去构造一个AudioPolicyManager对象。
4. AudioPolicyManager的构造方法里面会调用loadConfig方法去加载vendor/etc/audio_policy_configuration.xml文件把配置策略读到本地（通过adb shell dumpsys media_audio.policy可以dump出具体的所有配置）。在读取配置的之后会调用AudioPolicyManager::initialize方法（createAudioPolicyManager里面）来初始化AudioPolicyManager里面的各个东西。
    1. load libaudiopolicyenginedefault.so 赋值mEngine对象
    2. 调用onNewAudioModulesAvailableInt方法来加载Module（mHwModulesAll）对应的Device
        1. 调用AudioFlinger::loadHwModule方法获取到handle保存在hwModule，把hwModule保存在mHwModules里面
            > 通过这个handle可以在AudioFlinger里面找到对应的AudioHwDevice（通过handle为key保存在AudioFlinger::mAudioHwDevs里面）
            1. 这个方法会调用mDevicesFacoryHal的openDevice方法获取到一个可以操作底层的dev，保存在AudioHwDevice对象里面。
                1. 通过上面可以知道最后会调用到DevicesFactory::openDevice方法，之后会通过load某一个so，以primary为例，会load system/lib64/hw/audio.primary.sm6150.so（vendor/qcom/opensource/audio-hal/primary-hal/hal/audio_hw.c 高通710）
                2. AudioFlinger的mAudioHwDevs保存的是AudioHwDevice对象，AudioHwDevice对象里面的mHwDevice（DeviceHalInterface）指的是DeviceHalHidl，DeviceHalHidl里面的mDevice（IDevice）指的是PrimaryDevice，PrimaryDevice里面的mDevice（Device）指的是new Device(device)，这里的device指的是audio_hw_device_t
        2. 遍历hwModule里面的outputProfiles（inputProfiles），新建SwAudioOutputDescriptor对象，并调用其open方法，最终会调用到AudioFlinger::openOutput_l方法，这个方法会调用AudioHwDevice的openOutputStream方法最终调用到底层audio_hw_device_t的open_output_stream，并把获取到的outputStream保存在对应的PlaybackThread的mOutput里面（播放音频的时候其实就是Thread把音频数据写到mOutput里面）。
            > openOutput_l会自增一个output（audio_io_handle_t），并以其为key值把新建的PlaybackThread保存在mPlaybackThreads里面，调用SwAudioOutputDescriptor的open方法也会把这个key值返回。AudioPolicyManager也会以其为key值把SwAudioOutputDescriptor对象保存在mOutputs里面。
                > output->stream 为StreamOutHalHidl，StreamOutHalHidl里面的mStream（IStreamOut）为StreamOut.cpp，StreamOut里面的mStream（audio_stream_out_t）为audio_stream_out_t
        > 由上面可知，AudioPolicyManager::mHwModules跟AudioFlinger::mAudioHwDevs对应，AudioPolicyManager::mOutputs跟AudioFlinger的mPlaybackThreads对应，他们都有一样的key值。这样做的好处就是在播放过程中，通过调用getOutputForAttr获得到策略对应的output之后，就能在AudioFlinger里面找到对应的Thread;同理mAudioHwDevs与mHwModules。
5. AudioFlinger::createTrack 会新建一个sessionId，这个在AudioTrack跟PlaybackTrack::Track里面都会保存在mSessionId里面，两者一一对应。而方法中的registerPid方法则会在AudioFlinger里面的mClients新建并保存的Client，并且用pid跟AudioTrack::mClientPid一一对应的，Client对象也会保存到PlaybackThread::Track::mClient。SwAudioOutputDescriptor里面的mClients保存着以portId为key的TrackClientDescriptor，也与AudioFlinger::ThreadBase一一对应，保存着同样的portId，为mPortId。