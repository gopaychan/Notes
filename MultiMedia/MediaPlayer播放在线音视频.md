#### 主要流程（部分流程跟fd一致省略）
##### 1. setDataSource
1. java层因为涉及到网络所以传进来的数据除了Uri，还有headers和cookies
```setDataSource(String path, Map<String, String> headers, List<HttpCookie> cookies)```
2. 调用MediaHTTPService的createHttpServiceBinderIfNecessary方法，传入path和cookies，返回一个MediaHTTPService的binder（IMediaHTTPService）对象
3. 调用jni层的nativeSetDataSource方法传入获取到的binder，path，和headers对应的keys，values。(方法里面会把传进来的Binder对象转成JavaBBinder对象)
4. 调用nMP的setDataSource方法传入httpService（binder），和path，headers；接着调用MediaPlayerService::Client的setDataSource方法（方法里面会判断有没有联网的权限"android.permission.INTERNET"）
5. 调用NuPlayer的setDataSourceAsync方法，mSource为GenericSource，mDataSourceTyep为DATA_SOURCE_TYPE_GENERIC_URL，调用mSource的setDataSource方法，保存传入的uri，httpService和headers。

##### 2. prepare
1. GenericSource的onPrepareAsync方法里面跟fd不一样的地方是mUri部位空了，故会调用DataSourceFactory的CreateMediaHTTP方法传入mHTTPService，得到mHttpSource对象。
    1. 调用mHttpService（MediaHTTPService.java）的makeHTTPConnection得到MediaHTTPConnection对象conn（服务端是MediaHTTPConnection.java，这里是native层的client调用了java层的service，具体的数据传递看Binder.md）。新建MediaHTTP传入conn并返回该对象。
2. 这个时候mDataSource对象是没有初始化的，即为null。调用DataSourceFactory的CreateFromURI方法传入得到的mHttpSource对象以及mHTTPService，uri，headers等获得mDataSource对象。
    1. 调用httpSource的connect方法判断是否能够连接成功。
    2. 调用httpSource的getMIMEType方法获取contentType。
    3. 调用NuCachedSource2的Create方法传入httpSource等参数。

##### 3. NuCachedSource2的数据缓冲
1. NuCachedSource2的Create会post kWhatFetchMore消息，实际上就是调用了onFetch方法。
    1. 如果mFinalStatus（最后一次请求mSource的状态）不为OK且mNumRetriesLeft为0时则表明已经缓冲结束（eos）不再预取数据，mFetching设置成false。
    2. 如果没有正在取数据（!mFetching）且mFinalStatus==OK且mKeepAliveIntervalUs>0且```ALooper::GetNowUs() >= mLastFetchTimeUs + mKeepAliveIntervalUs```则设置keepAlive为true否则为false。
    3. 当keepAlive或者mFetching时，调用fetchInternal方法
        1. 检查mFinalStatus == OK 或者mNumRetriesLeft > 0 ，如果mFinalStatus!=OK则mNumRetriesLeft减一次，设置reconnect=true。
        2. 如果reconnect则调用mSource的reconnectAtOffset方法传入```mCacheOffset+mCache->tatalSize()```
        3. 如果mDisconnecting则播放结束设置```mNumRetriesLeft=0，mFinalStatus = ERROR_END_OF_STREAM```
        4. 如果reconnect得到ERROR_UNSUPPORTED或-EPIPE错误时设置```mNumRetriesLeft=0```
        5. 调用mCache的acquirePage（有个mFreePages，可以PageCache对象的重用，避免一直malloc申请内存）方法获得一个PageCache对象page。
        6. 调用mSource的readAt方法读取一个page大小的内容到page->mData里面 返回值为n。
        7. 如果n为0或mDisconnecting则播放结束设置```mNumRetriesLeft=0，mFinalStatus = ERROR_END_OF_STREAM```,releasePage，把page放到mFreePages中重用。
        8. 如果n小于0，且得到的是ERROR_UNSUPPORTED或-EPIPE错误设置```mNumRetriesLeft=0```。更新mFinalStatus=n
        9. 如果n大于0时，设置mNumRetriesLeft为最大值，mFinalStatus为OK，```page->mSize = n```，调用mCache的appendPage方法。
    4. 更新最后一次fetch时间```mLastFetchTimeUs = ALooper::GetNowUs()```
    5. 如果当前缓冲的总大小mCache->totalSize()大于设置的总大小是设置mFetching=false 停止缓冲，如果```mDisconnectAtHighwatermark
                    && (mSource->flags() & DataSource::kIsHTTPBasedSource)```disconnect mSource并且设置``` mFinalStatus = -EAGAIN```
    6. 当非keepAlive且非mFetching时 调用restartPrefetcherIfNecessary_l方法判断是否需要（当缓冲的数据达到最低水位mLowwaterThresholdBytes且满足其他情况时）重新设置mFetching为true。
    7. 当mFetching时
        1. ```mFinalStatus != OK && mNumRetriesLeft > 0```delayUs为3秒
        2. 否则delayUs为0
    8. 当!mFetching时delayUs为100毫秒
    9. postdelay kWhatFetchMore
2. Extractor调用mDataSource的readAt时（NuCachedSource2的readAt方法）
    1. 当readAt数据在mCache中包括时，调用mCache的copy方法复制对应位置的数据到data。
    2. 否则调用onRead方法，最终调用到readInternal方法
        1. 当readAt的offset比当前的mCacheOffset小或者大于mCacheOffset+mCache->totalSize的时候，重新设置mCacheOffset=offset，mNumRetriesLeft设置最大，mFetching为true（下一次Fetching的时候重新缓冲）。
        2. 否则返回当前offset之后剩下的缓冲。

##### 4. http&https请求
1. 媒体播放的Http请求主要在MediaHTTPConnection java类。
2. native调用MediaHTTPConnect的connect的时候会返回一个IMemory的binder对象，后面readAt的数据也是通过这个IMemory的pointer方法拿到的内存写进去跟读出来的（java层写，native层读）。
3. readAt的时候如果offset与之前的不一样就会调用seekTo方法。
    1. 调用URL类中openConnection的方法获得一个HttpURLConnection对象mConnection，该方法是通过一个URLStreamHandler对象handler来实现的，不同的协议handler不同，URL的构造方法会初始化这个handler，最后通过createBuiltinHandler方法根据不同protocol新建不同的URLStreamHandler对象，其中当protocol为https时，handler为```(URLStreamHandler)Class.forName("com.android.okhttp.HttpsHandler").newInstance();```这个时候返回的HttpURLConnection为HttpsURLConnectionImpl。
    2. 调用mConnection的getResponseCode方法
        1. 调用getResponse方法，会新建HttpEngine对象（后面的网络请求都是通过这个Engine去执行的），接着调用execute方法。execute方法里面会调用HttpEngine的sendRequest方法。
            1. sendRequest里面会先新建Request对象request
            2. 接着调用自己的connect方法，该方法里面会调用StreamAllocation对象streamAllocation（HttpEngine构造方法里面初始化的）的newStream方法
            3. 该方法会调用findHealthyConnection->findConnection方法获取一个RealConnection对象
            4. 接着调用RealConnection对像的connect->connectSocket方法，通过跟服务器进行socket连接之后封装连接的socket（rawSocket）成source（input）和sink（output）。
            5. newStream方法最后会根据具体条件生成Http2xStream或Http1xStream对象httpStream
        6. 调用HttpEngine的readResponse方法
            1. 调用readNetworkResponse方法，调用httpStream的readResponseHeaders方法。
            2. 接着调用httpStream的openResopnseBody方法获得RealResponseBody对象body，body对象的source为Okio.buffer(source)返回的RealBufferedSource对象，其中source为getTransferStream方法根据response获得的，一般为newFixedLengthSource方法返回的对象FixedLengthSource。
    3. getInputStream获得的是调用的是```response.getResponse().body().byteStream()```，response为httpEngine，getResopnse获得的是Reponse对象而其body为RealResponseBody，body的byteStream方法即RealResponseBody的该方法为```source().inputStream()```即RealBufferedSource的inputStream方法返回的。
        1. 这个inputStream方法里面返回了一个匿名内部类InputStream并重写了read等方法，read方法实际上是调用了FixedLengthSource的read方法，实际上是read的是Http1xStream的source，即RealConnection里面的source。