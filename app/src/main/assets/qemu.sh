#!/data/data/com.termux/files/usr/bin/bash

##################
clear
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
echo -e "${YELLOW}欢迎使用qemu-system-x86_64\\n
本脚本所使用的参数为简单设置，如需要请自行配置
因termux环境原因，暂不支持声卡
推荐win7系统${RES}
"
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
1) 安装qemu-system-x86_64(64位系统模拟器)，并联动更新模拟器所 需应用
2) 创建windows镜像目录
3) 启动qemu-system-x86_64模拟器
4) 退出\n"
read -r -p "请选择:" input
case $input in
	1)  echo -e "\n安装过程中，如遇到选择问题，请选(y)"
	sleep 3
	pkg update -y && pkg upgrade -y && pkg install x11-repo unstable-repo qemu-system-x86* -y
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
3) echo -e "${GREEN}请确认系统镜像已放入手机目录/xinhao/windows里${RES}"
        CONFIRM
        pkill -9 qemu-system-x86
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
cat >/dev/null<<-EOF
read -r -p "请选择声卡 1)es1370 2)sb16 3)ac97(推荐) 0)不加载" input
                        case $input in
                        1) set -- "${@}" "-device" "ES1370" ;;
                        2) set -- "${@}" "-device" "sb16" ;;
                        0) ;;
                        *) set -- "${@}" "-device" "AC97" ;;
                esac
		EOF
                set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$mir_name"
                set -- "${@}" "-drive" "file=fat:rw:/sdcard/xinhao/share"
#               set -- "${@}" "-audiodev" "pa","id=snd0"
#               set -- "${@}" "-device" "ich9-intel-hda"
#               set -- "${@}" "-device" "hda-output","audiodev=snd0"
#               set -- "${@}" "-boot" "order=c"
        set -- "qemu-system-x86_64" "${@}"
        "${@}" &
	sleep 2
	unset PULSE_SERVER
                unset QEMU_AUDIO_DRV
        ;;
4) unset PULSE_SERVER
                unset QEMU_AUDIO_DRV
		exit ;;
*) INVALID_INPUT && QEMU_SYSTEM ;;
esac
}
###################
MAIN() {
#export PULSE_SERVER=tcp:127.0.0.1:4713
#export QEMU_AUDIO_DRV=alsa
QEMU_SYSTEM
}
####################
MAIN "$@"
