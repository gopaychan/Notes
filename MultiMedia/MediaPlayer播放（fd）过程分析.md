#### 主要流程

##### 1 . 文件关系
- MediaPlayer.java -> android_media_MediaPlayer.cpp （连接native和java层的MediaPlayer） -> mediaplayer.cpp（native 层的MediaPlayer）-> MediaPlayerService.cpp（Client） ->（通过MediaPlayerFactory.cpp）-> NuPlayerDriver.cpp -> （通过AVNuFactory.cpp）-> NuPlayer.cpp -> GenericSource.cpp
##### 2. 时序图
![流程图](https://img-blog.csdnimg.cn/20190709175357157.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI3MTM2MTEx,size_16,color_FFFFFF,t_70)
##### 3. setDataSource（[参考](https://blog.csdn.net/qq_27136111/article/details/94594673) java层省略）
1. native MediaPlayer（下称nMP，是一个可跨进程的对象<font color=#ff0000>IMediaPlayerClient</font>） 调用 MediaPlayerService（下称MPS）的create方法创建一个跨进程的player（<font color=#ff0000>IMediaPlayer</font> MPS端的Client，下称MPSC的Bp）并保存为mPlayer；同时也把自己传递过去保存在MPSC的mClient（IMediaPlayerClient）里面，用于远程的回调。
2. 调用player（MPSC）的setDataSource方法 
3. MPSC调用MediaPlayerFactory（下称MPF）的getPlayerType方法，根据相应的DataSource类型获取对应的PlayerType，Android9.0的Type只有NU_PLAYER（5.1.1还有STAGEFRIGHT_PLAYER，SONIVOX_PLAYER，TEST_PLAYER）
4. MPSC调用setDataSource_pre方法做一些设置DataSource之前的准备。
    a. 根据PlayerType 调用MPF的createPlayer方法，最终调用了NuPlayerFactory的createPlayer生成NuPlayerDriver（<font color=#ff0000>MediaPlayerBase</font>；把pid保存在里面，下称NPD），NPD通过AVNuFactory生成NuPlayer保存在mPlayer里面。
    b. 设置分离器binder（media.extractor）和解码器binder（omx）的死亡通知监听。
    c. 调用NPD的setAudioSink 保存新建的AudioOutput。
5. MPSC调用setDataSource_post保存生成的NPD到mPlayer里面。
6. 调用NuPlayerDriver的setDataSource方法，实际上是调用了NuPlayer的setDataSourceAsync方法，根据不同的播放DataSource生成不同的<font color=#ff0000>Nuplayer::Source</font>（GenericSource，HttpLiveSource，RtspSource，StreamingSource）并调用它的setDataSource方法（保存DataSource，如：fd，offset，length），最后把生成的source保存在mSource里面。
7. 通知NPDsetDataSource setDataSource completed 设置mState为 STATE_UNPREPARED。
#####4. prepare（[参考](https://blog.csdn.net/qq_27136111/article/details/95357167)）
1. 同步的话调用nMP的prepare方法，异步调用nMP的prepareAsync方法（prepare 先设置 mPrepareSync=true 后调用prepareAsync_l方法，再用mSignal.wait等待方法执行结束；prepareAsync则直接调用prepareAsync_l方法）
2. 调用MPSC的prepareAsync方法，最后调用到NuPlayer的mSource（GenericSource）的prepareAsync方法

