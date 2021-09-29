1. 开机或磁盘重新挂载的时候媒体库会重新扫描
2. 扫描内部磁盘VOLUME_INTERNAL的最后，会调用RingtoneManager.ensureDefaultRingtones确保notification，alarm，ringtone的默认音效设置了。
    1. ensureDefaultRingtones：
        1. 通过判断对应的ringtone_set等是否为0,确定有没有设置默认音效。
        2. 没有设置默认音效的通过ro.config.ringtone获取音效名字，并通过MediaProvider获取对应的音效Uri
        3. 调用RingtoneManager.setActualDefaultRingtoneUri，保存默认音效settings uri，并把对应音频文件复制到data/system_de/ringtone目录下，官方解释是：“Stream selected ringtone into cache so it's available for playback when CE storage is still locked”
        4. 把对应的ringtone_set等设置为1。
3. ModernMediaScanner：主要的扫描工作是在这个里面进行的，主要是visitFile方法。
4. MtpDatabase：也会调用媒体库扫描，不过是通过MediaStore.scanFile方法调用到MediaProvider的（通过ContentProvider相关机制）
5. 在ThumbnailUtils类里面会读取音频的封面，并且在scan的最后，会调用applyPending，最终会调用到insertFile，insertFile的最前面判断当前file为audio的时候会调用computeAudioKeyValues来根据专辑名之类的计算出专辑id跟key。