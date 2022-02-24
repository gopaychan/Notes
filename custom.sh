function develop9()
{
    adbroot
    adb shell settings put global development_settings_enabled 1
    adb shell am start -n com.android.settings/.Settings
    adb shell am start com.android.settings/'.Settings\$DevelopmentSettingsDashboardActivity'
}

function develop7()
{
    adbroot
    adb shell am start -n com.android.settings/.DevelopmentSettings
}

function del_encode_log()
{
    rm `ls | grep -v .plt`
}

function __ungzFile()
{
    for gz in *.gz
    do
        if [ -s $gz ]
        then
            echo extract $gz
            des=$(basename -s .tar.gz $gz)
            rm -rf $des
            mkdir $des
            tar -zxf $gz -C $des
        else
            echo '\t' $gz is empty
        fi
    done
}

function prelog()
{
    __ungzFile .
    rm *.gz
    logdecrypt .
}

function push_framework_jar()
{
    adbroot
    out_path=$1
    echo $out_path
    adb push $out_path/system/framework/framework.jar system/framework/
    adb push $out_path/system/framework/boot-framework.vdex system/framework
    adb push $out_path/system/framework/arm/boot-framework.art /system/framework/arm
    adb push $out_path/system/framework/arm/boot-framework.art.rel /system/framework/arm
    adb push $out_path/system/framework/arm/boot-framework.oat /system/framework/arm
    adb push $out_path/system/framework/arm/boot.art system/framework/arm/
    adb push $out_path/system/framework/arm/boot.oat system/framework/arm/
    adb push $out_path/system/framework/arm64/boot.art system/framework/arm64/
    adb push $out_path/system/framework/arm64/boot.oat system/framework/arm64/
    adb push $out_path/system/framework/arm64/boot-framework.art /system/framework/arm64
    adb push $out_path/system/framework/arm64/boot-framework.art.rel /system/framework/arm64
    adb push $out_path/system/framework/arm64/boot-framework.oat /system/framework/arm64
}

function __isencryptapk()
{
    apk_name=$1
    if [ -z $apk_name ]
    then
        apk_name="base.apk"
    fi
    
    apksignal=$(od -N4 -t x4 $apk_name | head -1 | sed 's/0000000 //g')
    if [ $apksignal == "034b5042" ]; then
        return 1
    fi
    return 0
}

function unzipapk()
{
    apk_name=""
    unzip_option=""
    jadx_option=""

    case $# in
        0)
            apk_name="base.apk"
            ;;
        1) 
            
            if [[ $1 == *.apk ]] || [[ $1 == *.dex ]] ||[[ $1 == *.jar ]] || [[ $1 == *.class ]] ; then
                apk_name=$1
            else
                unzip_option=$1
            fi
            ;;
        2)
            apk_name=$2
            unzip_option=$1
            ;;
        *)
            echo "input arg failure"
            return
    esac

    output_path=$(echo $apk_name | sed 's/.apk//g; s/.dex//g; s/.jar//g; s/.class//g')
    if [ -d $output_path ]; then
        rm -rf $output_path
    fi
    
    apk_decrypt_path=""
    __isencryptapk $apk_name
    if [ $? -eq 1 ]; then
        apk_decrypt_path=$output_path"-d.apk"
        apkencrypt $apk_name $apk_decrypt_path d
        apk_name=$apk_decrypt_path
    fi

    case $unzip_option in
        "-r")
            jadx_option="-s"
            ;;
        "-s")
            jadx_option="-r"
            ;;
        "-e")
            jadx_option="-e"
            ;;
        "-h" | "-help")
            echo "usage: unzipapk [options] [input file] (.apk, .dex, .jar or .class)"
            echo "options:"
            echo "  -r,                        - decode resources"
            echo "  -s,                        - decompile source code"
            echo "  -e,                        - save as android gradle project"
            return
            ;;
        *)
            # echo "unzip default"
    esac
    jadx -d $output_path $jadx_option $apk_name 
    # apktool -s d $apk_name -o $output_path

    if [ ! -z $apk_decrypt_path ]; then
        rm $apk_decrypt_path
    fi 
}

function systrace()
{
    argc=$#
    time=3
    name="trace.html"
    other="sched gfx view wm input disk idle freq power"

    if [ $argc -ge 1 ]; then
        argv=$*
        argv=(${=argv})
        i=1

        for (( ; i <= argc; i++ )); do
            v=$argv[$i]
            if [[ ! $v == -* ]]; then
               name=$v
               ((i++))
               break
            elif [[ $v == -t ]]; then
                if [[ $i+1 -le argc ]]; then
                    ((i++))
                    time=$argv[$i]
                fi
            fi
        done

        if [[ i -le argc ]]; then
            other=""
        fi
        for (( ; i <= argc; i++ )); do
            other=$other" "$argv[$i]
        done
    fi

    python2.7 ~/Documents/Android/platform-tools/systrace/systrace.py --time=$time -o $name $other
}

function com() 
{
	ports_USB=$(ls /dev/ttyUSB*)
	ports_ACM=$(ls /dev/ttyACM*)  #arduino
	ports="$ports_USB $ports_ACM"
	datename=$(date +%Y%m%d-%H%M%S)
	select port in $ports;do
		if [ "$port" ]; then
		    echo "You select the choice '$port'"
            sudo minicom -D "$port" "$@"
		    break
		else
		    echo "Invaild selection"
	    fi
	done
}

function plogcat()
{
    pid=$1
    if [ -z $pid ]
    then
        adb shell logcat -c && adb shell logcat -v time
    else
        adb shell logcat -c && adb shell logcat -v time | grep "\( $pid\)"
    fi
}

function utf82gbk()
{
    python3 ~/bin/Utils.py utf8ToGbk $1
}

function gbk2utf8()
{
    python3 ~/bin/Utils.py gbkToUtf8 $1
}

function adb-connect()
{
    ip=$(adb shell ifconfig | grep "wlan0" -A 1 | awk '{a[2]=$0; if(NR == 2) {print a[2]}}' | sed 's/:/ /g' | awk '{print $3}')
    if [ -z $ip ]
    then
        echo "You didn't turn on the device's wifi"
    else
        local_ip=$(ifconfig | grep "enp2s0" -A 1 | awk '{a[2]=$0; if(NR == 2) {print a[2]}}' | sed 's/:/ /g' | awk '{print $2}')

        id_pre=${ip:0:6}
        local_ip_pre=${local_ip:0:6}
        if [ $id_pre = $local_ip_pre ]
        then
            adb tcpip 5555
            sleep 1s
            adb connect $ip
        else
            echo "Not on the same network"
        fi
    fi
}

function ld-pkgname()
{
    pkg_array_string=$(adb shell pm list package $1 | sed 's/:/ /g' | awk '{print $2}')
    if [ -z $pkg_array_string ]; then
        echo "Didn't find the apk, please check if you input a correct package name"
    else
        echo $pkg_array_string
    fi
}

function __selectpkgname()
{
    pkg_name=$1
    if [ -z $pkg_name ] || [ $# -lt 2 ]
    then
        echo "You didn't provide an accurate or vague package name"
        return -1
    else
        array_str=(${pkg_name//$'\n'/ })
        if ! echo $array_str | grep -q "\." ; then
            # echo $array_str
        else
            array=(${=array_str}) #String to String array
        fi
        if [[ ${#array[@]} -eq 1  ]]; then
            accurate_pkg_name=$(ld-pkgname $pkg_name)
            array_str=(${accurate_pkg_name//$'\n'/ })
            if ! echo $array_str | grep -q "\." ; then
                echo $array_str
                return -1
            fi
            array=(${=array_str}) #String to String array
        elif [[ ${#array[@]} -gt 1 ]]; then
            #statements
        fi







        accurate_pkg_name=$(ld-pkgname $pkg_name)

        array_str=(${accurate_pkg_name//$'\n'/ })
        if ! echo $array_str | grep -q "\." ; then
            echo $array_str
            return -1
        fi

        array=(${=array_str}) #String to String array
        selection=""

        if [ ${#array[@]} -eq 1 ]; then
            selection=${array[1]}
        else 
            for var in ${array[@]}; do
                if [ "$pkg_name" = "$var"  ]; then
                    selection=$var
                fi
            done
        fi

        if [ -z $selection ]; then
            echo "pull apk... pick a package name:"
            pkg_name_index=0;
            for var in ${array[@]}
            do
                ((pkg_name_index++))
                echo $pkg_name_index". "$var
            done
            echo ""
            echo -n "Which would you like? [${array[1]}] "
            read selection

            if [ -z $selection ]; then
                selection=${array[1]}
            elif (echo -n $selection | grep -q -e "^[0-9][0-9]*$"); then
                if [ $selection -le ${#array[@]} ]; then
                    selection=${array[($selection)]}
                else
                    selection=${array[1]}
                fi
            fi
            echo ""
        fi

        
        echo "The apk package name selected is "\"$selection\"
        eval $2=$selection
        return 0
    fi
}

function ld-apkpath()
{
    select_pkg_name=""
    __selectpkgname $1 select_pkg_name
    if [ $? -ge 0 ]; then
        app_path=$(adb shell pm path $select_pkg_name | sed 's/:/ /g' | awk '{print $2}')
        if [ $# -eq 2 ]; then
            eval $2=$app_path
        else
            echo -E $app_path
        fi
        return 0
    fi
    return -1
}

function pullapk()
{
    app_path=""
    ld-apkpath $1 app_path
    if [ $? -ge 0 ]; then
        adb pull $app_path
    else
        pullfocapk
    fi
}

function ld-focusapp()
{
    sdk=$(adb shell getprop ro.build.version.sdk)
    if [[ $sdk -lt 30 ]] && [[ $sdk -ge 24 ]]; then
        adb shell dumpsys window | grep mFocusedApp | awk '{a[NR]=$0; if(NR == 1) {print a[NR]}}' | awk '{print$4}'
    elif [ $sdk -ge 30 ]; then
        adb shell dumpsys window | grep mFocusedApp=ActivityRecord | awk '{print $3}'
    else
        echo "Unsupport"
    fi
}

function __ld-focusapp-pkgname
{
    ld-focusapp | sed 's/\// /g' | awk '{print $1}'
}

function pullfocapk()
{
    pkg_name=$(__ld-focusapp-pkgname)
    echo $pkg_name
    pullapk $pkg_name
}

function apkinfo()
{
    is_need_permission=0
    if [ $# -eq 2 ]; then
        if [ $1 == "-p" ]; then
            is_need_permission=1
        fi
        apk_path=$2
    elif [ $# -eq 1 ]; then
        apk_path=$1
    elif [ $# -eq 0 ]; then
        apk_path=$(__ld-focusapp-pkgname)
    fi

    if [ -z $apk_path ]
    then
        echo "You didn't provide an apk path or package name"
    elif [[ $apk_path == *.apk ]]; then
        java -jar ~/bin/GetAPKInfo.jar $apk_path
        # keytool -printcert -jarfile $apk_path | grep SHA1 -C 1 | sed 's/\t//g' | sed 's/Certificate fingerprints:/֤?ָ?: /g' | sed 's/SHA/ SHA/g' 
    else 
        select_pkg_name=""
        __selectpkgname $apk_path select_pkg_name

        if [ $? -ge 0 ]; then
            apkpath=""
            ld-apkpath $select_pkg_name apkpath > /dev/null 2>&1

            if [ $is_need_permission -eq 1 ]; then
                permission_sed="/Queries:/,\$d; /Hidden system packages:/,\$d; /Renamed packages:/,\$d; " #  User 0: 
            else
                permission_sed="/requested permissions:/,\$d; /declared permissions:/,\$d; /install permissions:/,\$d; /Queries:/,\$d;"
            fi
            delete_info_sed="/applicationInfo=/d; /resourcePath=/d; /pkg=Package/d; /secondaryCpuAbi=/d; /splits=/d; /Package \[/d; /usesLibraries:/,/timeStamp=/d; /queriesPackages=/d; /forceQueryable=/d; /lastUpdateTime=/d; /installPermissionsFixed=/d; /ignatures=/d; /pkgFlags=/d;"
            format_sed="s/    /  /; s/=/: /g; s/: : /==/g;"

            appinfo_orgin=$(adb shell pm dump $select_pkg_name)
            apkinfo=$(echo $appinfo_orgin | sed -e $permission_sed'0,/Packages:/d; '$delete_info_sed)
            apkinfo_codepath=$(echo $apkinfo | grep 'codePath=' | sed 's\codePath=\\; s\    \\g')
            apkinfo_codepath=$(eval echo $apkinfo_codepath)

            flags_str=$(echo $apkinfo | grep "    flags=" | sed 's/    flags=\[ //; s/ ]//')
            flags_array=(${=flags_str})
            is_system=0
            is_presistent=0
            is_system_update=0
            for var in ${flags_array[@]}; do
                case $var in
                    "SYSTEM")
                    is_system=1
                    ;;
                    "PERSISTENT")
                    is_presistent=1
                    ;;
                    "UPDATED_SYSTEM_APP")
                    is_system_update=1
                    ;;
                    *)
                esac
            done

            is_system_sign="×"
            if [[ is_system -eq 1 ]]; then
                is_system_sign="✓"
            fi
            is_presistent_sign="×"
            if [[ is_presistent -eq 1 ]]; then
                is_presistent_sign="✓"
            fi
            is_system_update_sign="×"

            hide_apkinfo=""
            if [[ is_system_update -eq 1 ]]; then
                is_system_update_sign="✓"
                hide_apkinfo=$(adb shell pm dump $select_pkg_name | sed '0,/Hidden system packages:/d'| sed '/requested permissions:/,$d; /declared permissions:/,$d; '$delete_info_sed)
            fi

            echo ""
            echo "应用信息:"
            echo $apkinfo | sed 's\'$apkinfo_codepath'\'$apkpath'\' | sed '1a \ \ isSystem: '$is_system_sign'\  isSystemUpdate: '$is_system_update_sign'\  isPersistent: '$is_presistent_sign | sed $format_sed
            if [ $is_need_permission -eq 0 ]; then
                install_pkgname=$(echo $appinfo_orgin | grep installerPackageName | sed $format_sed)
                if [ ! -z $installerPackageName ]; then
                    echo $installerPackageName
                fi
            fi
            if [[ -n $hide_apkinfo ]]; then
                echo ""
                echo "Hidden Package:"
                echo $hide_apkinfo | sed $format_sed
            fi
        fi
    fi
}

function ld-pkgnamebyuid()
{
    uid=$1
    packages=$(adb shell pm dump p | grep "Package \[" -A 1 | sed 's/([0-9,a-z,A-Z]\+)//g; s/Package \[//g; s/\] ://g; s/    /  /g; s/--//g;')
    
    if [ ! -z $uid ]; then
        echo $packages | grep "userId=[0-9]*"$uid -B 1
    else
        echo $packages
    fi
}

function videocrypt()
{
    if [[ $# == 3 ]]; then
        src_video=$1
        des_video=$2
        crypt_video=$3
    else
        echo "Parameter error !!!!!"
        echo "example: videocrypt \$1 \$2 d|e"
        return
    fi
    if [[ $crypt_video == "e" ]]; then
        videoencrypt_short $src_video $des_video
    elif [[ $crypt_video == "d" ]]; then
        ffmpeg_custom -i $src_video -acodec copy -vcodec copy $des_video
    fi
}

function videoinfo()
{
    if [[ $# == 1 ]]; then
        video_name=$1
        videosignal=$(od -N8 -t x4 $video_name | head -1 | sed 's/0000000 //g')
        if [[ $videosignal == "42424545 444d424b" ]]; then
            echo "BMD Video !!!!!!"
        fi
        ffprobe_custom -i $1 -hide_banner
    fi
}