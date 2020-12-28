#!usr/bin/bash

clear
#################

LOGIN() {
pulseaudio --start &
echo "" &
echo "欢迎来到bullseye-qemu系统" &
unset LD_PRELOAD
command="proot"
command+=" --kill-on-exit"
command+=" --link2symlink"
command+=" -S bullseye-qemu"
command+=""
command+=" -b bullseye-qemu/root:/dev/shm"
## uncomment the following line to have access to the home directory of termux
#command+=" -b /data/data/com.termux/files/home:/root"
## uncomment the following line to mount /sdcard directly to /
command+=" -b /sdcard"
command+=" -w /root"
command+=" /usr/bin/env -i"
command+=" HOME=/root"
command+=" USER=root"
command+=" PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games"
command+=" TERM=xterm-256color"
command+=" LANG=C.UTF-8"
command+=" /bin/bash --login"
com="$@"
if [ -z "$1" ];then
    exec $command
else
    $command -c "$com"
fi
}
##################
SYS_DOWN() {
sys_name=bullseye-qemu
                DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/debian/bullseye/arm64/default/"
		BAGNAME="rootfs.tar.xz"
        if [ -e ${BAGNAME} ]; then
                rm -rf ${BAGNAME}
        fi
        curl -o ${BAGNAME} ${DEF_CUR}
                VERSION=`cat ${BAGNAME} | grep href | tail -n 2 | cut -d '"' -f 4 | head -n 1`
                curl -o ${BAGNAME} ${DEF_CUR}${VERSION}${BAGNAME}
                if [ $? -ne 0 ]; then
                        echo -e "${RED}下载失败，请重输${RES}\n" && MAIN
                fi
                if [ -e $sys_name ]; then
                        rm -rf $sys_name
                fi
                mkdir $sys_name
#tar xvf rootfs.tar.xz -C ${BAGNAME}
echo -e "${BLUE}正在解压系统包${RES}"
                tar xf ${BAGNAME} --checkpoint=100 --checkpoint-action=dot --totals -C $sys_name 2>/dev/null
                rm ${BAGNAME}
                echo -e "${BLUE}$sys_name系统已下载，文件夹名为$sys_name${RES}"
        echo "127.0.0.1 localhost" > $sys_name/etc/hosts
        rm -rf $sys_name/etc/resolv.conf &&
        echo "nameserver 223.5.5.5
nameserver 223.6.6.6" >$sys_name/etc/resolv.conf
        echo "export  TZ='Asia/Shanghai'" >> $sys_name/root/.bashrc
	echo "deb http://mirrors.ustc.edu.cn/debian sid main contrib non-free" >$sys_name/etc/apt/sources.list
cat >/dev/null<<-EOF
echo 'deb http://mirrors.bfsu.edu.cn/debian/ bullseye main contrib non-free
deb http://mirrors.bfsu.edu.cn/debian/ bullseye-updates main contrib non-free
deb http://mirrors.bfsu.edu.cn/debian/ bullseye-backports main contrib non-free
deb http://mirrors.bfsu.edu.cn/debian-security bullseye-security main contrib non-free' >$sys_name/etc/apt/sources.list
EOF
cp utqemu.sh $sys_name/root/utqemu.sh
sed -i "s/qemu-system-x86-64-headless/qemu-system-x86/" $sys_name/root/utqemu.sh
sed -i 's/qemu-system-i386-headless/-y \&\& apt reinstall pulseaudio/' $sys_name/root/utqemu.sh
sed -i '/^MAIN()/,$d' $sys_name/root/utqemu.sh
echo 'MAIN(){
QEMU_SYSTEM
}
MAIN "$@"' >>$sys_name/root/utqemu.sh
sed -i "1i\. utqemu.sh" $sys_name/etc/profile 
}
##################
######################
YELLOW="\e[1;33m"
GREEN="\e[1;32m"
RED="\e[1;31m"
BLUE="\e[1;34m"
PINK="\e[0;35m"
WHITE="\e[0;37m"
RES="\e[0m"
#####################

#####################
INVALID_INPUT() {
echo -e "${RED}Invalid input...${RES}" \\n
echo "choose again"
sleep 1
}                                                          #####################
CONFIRM() {                                                read -r -p "按回车键继续" input
case $input in
*) ;; esac
}
#####################
##################
QEMU_SYSTEM() {
echo -e "
1) 安装qemu-system-x86_64，并联动更新模拟器所需应用\n${YELLOW}(由于qemu的依赖问题，安装过程可能会失败，请尝试重新安装)${RES}
2) 创建windows镜像目录
3) 启动qemu-system-x86_64模拟器
4) 退出\n"
read -r -p "请选择:" input
case $input in
	1)  echo -e "${YELLOW}安装过程中，如遇到询问选择，请输(y)，安装过程极易出错，请重试安装${RES}"
	sleep 2
	apt update && apt --fix-broken install -y && apt install qemu-system-x86-64-headless qemu-system-i386-headless -y
        QEMU_SYSTEM
        ;;
2) echo -e "创建windows镜像目录及共享目录\n"
        if [ ! -e "/sdcard/xinhao/windows" ]; then
                mkdir -p /sdcard/xinhao/windows
        fi
        if [ ! -e "/sdcard/xinhao/share/" ]; then
                mkdir -p /sdcard/xinhao/share
        fi
        ls /sdcard/xinhao/windows
        if [ "$?" != "0" ]; then
                echo -e "${RED}创建目录失败${RES}"
        else
                echo -e "${GREEN}手机根目录下已创建/xinhao/windows文件夹，请把系统镜像放进这个目录里\n共享目录是/xinhao/share(目录内总文件大小不能超过500m)\n${RES}"
        fi
        CONFIRM
        QEMU_SYSTEM
        ;;
3) export PULSE_SERVER=127.0.0.1
	echo -e "${GREEN}请确认系统镜像已放入手机目录/xinhao/windows里${RES}"
        CONFIRM
        pkill -9 qemu-system-x86
	pkill -9 qemu-system-i38
        case $(dpkg --print-architecture) in
                x86_64|amd64|i*86|x86)
                        echo -e "${YELLOW}检测到你的cpu支持kvm加速
,如你已加入kvm内核,可尝试开启${RES}
                        1) 使用kvm加速(如启动失败，请选取消加速)
                        2) 取消加速\n"
                        read -r -p "请选择:" input
                        case $input in
                                1) set -- "${@}" "-enable-kvm" ;;
                                *) ;;
esac
 ;;
                        *)
                                set -- "${@}" "--accel" "tcg,thread=multi" ;;
                esac
		echo -e "\n请选择启动哪个模拟器\n
        1) qemu-system-x86_64
        2) qemu-system-i386\n"
        read -r -p "E(exit) M(main)请选择:" input
        case $input in
                1) QEMU_SYS=qemu-system-x86_64 ;;
                2) QEMU_SYS=qemu-system-i386 ;;
                [Ee]) exit 1 ;;
                [Mm]) MAIN ;;
                *) INVALID_INPUT
                        QEMU_SYSTEM ;;
        esac
        echo -n "请输入系统镜像全名（例如andows.img）mir_name:"
        read mir_name
        echo -n "请输入模拟的内存大小，以m为单位（1g=1024m ， 例如512）mem:"
        read mem
        set -- "${@}" "-m" "$mem"
        set -- "${@}" "-rtc" "base=localtime"
        set -- "${@}" "-vnc" ":0"
        read -r -p "请选择cpu 1)core2duo 2)athlon 3)pentium2 4)n270" input
        case $input in
        1) set -- "${@}" "-cpu" "core2duo"
                set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1" ;;
        2) set -- "${@}" "-cpu" "athlon"
                set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1" ;;
        3) set -- "${@}" "-cpu" "pentium2"
                set -- "${@}" "-smp" "1,cores=1,threads=1,sockets=1" ;;
        4) set -- "${@}" "-cpu" "n270"
                set -- "${@}" "-smp" "2,cores=1,threads=2,sockets=1" ;;
        *)      set -- "${@}" "-cpu" "Skylake-Server-IBRS"
                set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1" ;;
esac
        read -r -p "请选择显卡 1)cirrus 2)vmware" input
        case $input in
                1) set -- "${@}" "-vga" "cirrus" ;;
                *) set -- "${@}" "-vga" "vmware" ;;
        esac
        read -r -p "请选择网卡 1)e1000 2)rtl8139 0)不加载" input
        case $input in
                        1) set -- "${@}" "-net" "user"
                                set -- "${@}" "-net" "nic,model=e1000" ;;
                        0) ;;
                        *) set -- "${@}" "-net" "user"
                                set -- "${@}" "-net" "nic,model=rtl8139" ;;
                esac
                set -- "${@}" "-usb" "-device" "usb-tablet"
                read -r -p "请选择声卡 1)es1370 2)sb16 3)ac97(推荐) 0)不加载" input
                        case $input in
                        1) set -- "${@}" "-device" "ES1370" ;;
                        2) set -- "${@}" "-device" "sb16" ;;
                        0) ;;
                        *) set -- "${@}" "-device" "AC97" ;;
                esac
                set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$mir_name"
                set -- "${@}" "-drive" "file=fat:rw:/sdcard/xinhao/share"
#               set -- "${@}" "-audiodev" "pa","id=snd0"
#               set -- "${@}" "-device" "ich9-intel-hda"
#               set -- "${@}" "-device" "hda-output","audiodev=snd0"
#               set -- "${@}" "-boot" "order=c"
        set -- "$QEMU_SYS" "${@}"
        "${@}" &
        ;;
4) exit 1 ;;
*) INVALID_INPUT && QEMU_SYSTEM ;;
esac
}
###################
MAIN() {
	echo -e "请选择qemu-system-x86运行环境\n
	1) termux(utermux)环境运行 暂不支持声音输出
	2) 单独系统运行 需另外下载约占500m的系统
	3) 退出\n"
	read -r -p "请选择:" input
	case $input in
		2) uname -a | grep 'Android' -q
if [ $? == 0 ]; then
	ls bullseye-qemu 2>/dev/null
fi
	if [ $? != 0 ]; then
		SYS_DOWN
	fi
	LOGIN ;;
	1) QEMU_SYSTEM ;;
	3) exit 1 ;;
	*) INVALID_INPUT
		MAIN ;;
esac
}
####################
MAIN "$@"
