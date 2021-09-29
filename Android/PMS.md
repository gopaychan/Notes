1. PackageManagerService的构造方法里面会调用多次scanDirTracedLI扫描各个特定目录下的apk，当传入的scanFlags包括SCAN_AS_SYSTEM的话，扫描出来的ParsedPackage对象就会setSystem，其他的flag同理。在应用起来的时候会通过这个对象去给ApplicationInfo的flags赋值（如：调用PackageInfoUtils.appInfoFlags）。
2. 应用安装：
3. 应用卸载：
4. 扫描：
    1. 一般安装扫描：installPackagesTracedLI -> installPackagesLI
        1. preparePackageLI
        2. scanPackageTracedLI(ParsedPackage parsedPackage,...)
    2. 开机扫描：scanDirTracedLI -> scanDirLI -> addForInitLI（调用这个方法前已经解析出了ParsedPackage） -> scanPackageNewLI -> scanPackageOnlyLI

5. scanPackageOnlyLI里面会调用derivePackageAbi方法把apk里面的so提取到某一目录（第一次开机或者系统升级的），当apk zip包出现问题，或者App Manifest里面有extractNativeLibs=false的标志时，不会提取。