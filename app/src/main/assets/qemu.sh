#!/data/data/com.termux/files/usr/bin/bash
cd $(dirname $0)
clear
echo -e "\n\e[33m更新内容
	新增让手机变成网页服务器，使模拟系统可以访问手机，突破共享文件夹500m的限制
	qemu5.0以上版本增加硬盘接口sata选项，仍在测试阶段(读取速度有待考证)
	qemu5.0以上版增加硬盘接口virtio选项，系统需已安装virtio驱动，否则无法启动，同样是测试阶段
	修改了一些细节\e[0m\n
注意事项
		
	本脚本是方便大家简易配置，所有参数都是经多次测试通过，可运行大部分系统，由于兼容问题，性能不作保证，专业玩家请自行操作
	qemu5.0以上版本较旧版本多主板q35，硬盘接口的选项
	如遇到使用异常，请尝试所有选择项直接回车以获得默认参数
	q35主板与sata，virtio硬盘接口由于系统原因，可能导致启动不成功
	运行速度不稳定，受termux(utermux)环境影响，偶尔模拟出来的运行速度极慢
	声音输出（不支持termux与utermux环境下的模拟）
	sdl输出显示，需先开启xsdl(不支持termux与utermux环境）
	qemu5.0以下模拟xp较好，qemu5.0以上对win7以上模拟较好\n"
	if [ $(command -v qemu-system-x86_64) ]; then
		echo -e "\e[33m检测到你已安装qemu-system-x86，版本是\e[0m"
		qemu-system-x86_64 --version | head -n 1
	fi
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
command+=" -b /sdcard"
command+=" -b bullseye-qemu/root:/dev/shm"
command+=" -b /sdcard:/root/sdcard"
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
	echo -e "${YELLOW}即将下载系统(约占500m空间)${RES}"
	sleep 2
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
curl -O http://down.archserver.top:81/utqemu.sh 2>/dev/null
cp utqemu.sh $sys_name/root/utqemu.sh
sed -i "s/qemu-system-x86-64-headless/qemu-system-x86 xserver-xorg x11-utils/" $sys_name/root/utqemu.sh
sed -i 's/qemu-system-i386-headless/-y \&\& apt --reinstall install pulseaudio/' $sys_name/root/utqemu.sh
#sed -i '/^MAIN()/,$d' $sys_name/root/utqemu.sh
#echo 'MAIN(){
#QEMU_SYSTEM
#}
#MAIN "$@"' >>$sys_name/root/utqemu.sh
echo "bash utqemu.sh" >>$sys_name/etc/profile
echo -e "${YELLOW}系统已下载，请登录系统继续完成qemu的安装${RES}"
sleep 2
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
PULSEAUDIO() {
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
	dpkg -l | grep pulseaudio -q 2>/dev/null
if [ $? != 0 ]; then
	echo -e "检测到你未安装pulseaudio，为保证声音正常输出，将自动安装"
	sleep 2
	apt update && apt install pulseaudio -y
if grep -q "anonymous" ${PREFIX}/etc/pulse/default.pa ;
then
        echo "module already present"
else
        echo "load-module module-native-protocol-tcp auth-ip-acl=127.0.0.1 auth-anonymous=1" >> ${PREFIX}/etc/pulse/default.pa
                                fi
if grep -q "exit-idle" ${PREFIX}/etc/pulse/daemon.conf ; then
sed -i '/exit-idle/d' ${PREFIX}/etc/pulse/daemon.conf
echo "exit-idle-time = -1" >> ${PREFIX}/etc/pulse/daemon.conf
fi
fi
	fi
}
##################
WEB_SERVER() {
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
		if [ ! $(command -v python) ]; then
			echo -e "\n检测到你未安装所需要的包python,将先为你安装上"
			apt update && apt install python
		fi
		else
if [ ! $(command -v python3) ]; then
                echo -e "\n检测到你未安装所需要的包python,将先为你安装上"
                sleep 2
                apt update && apt install python3 python3-pip -y && mkdir -p /root/.config/pip && echo "[global]
index-url = https://pypi.tuna.tsinghua.edu.cn/simple" >/root/.config/pip/pip.conf
        fi
		fi
        `ip a | grep 192 | cut -d " " -f 6 | cut -d "/" -f 1` 2>/dev/null
        if [ $? != 0 ]; then
                IP=$(ip a | grep 192 | cut -d " " -f 6 | cut -d "/" -f 1)
        else
                `ip a | grep inet | grep rmnet | cut -d "/" -f 1 | cut -d " " -f 6` 2>/dev/null
                if [ $? -ne 0 ]; then
                        IP=$(ip a | grep inet | grep rmnet | cut -d "/" -f 1 | cut -d " " -f 6)
                else
                        IP=$(ip a | grep inet | grep wlan | cut -d "/" -f 1 | cut -d " " -f 6)
                        fi
        fi
        echo -e "已完成配置，请尝试用浏览器打开并输入地址\n
        ${YELLOW}http://$IP:8080${RES}\n
        如需关闭，请按ctrl+c，然后输pkill python3或直接exit退出shell\n"
        python3 -m http.server 8080 &
        sleep 2
}

##################
QEMU_SYSTEM() {
echo -e "
1) 安装qemu-system-x86_64，并联动更新模拟器所需应用\n${YELLOW}(由于qemu的依赖问题，安装过程可能会失败，请尝试重新安装)${RES}
2) 创建windows镜像目录
3) 启动qemu-system-x86_64模拟器
4) 让termux成为网页服务器
5) 退出\n"
read -r -p "请选择:" input
case $input in
	1)  echo -e "${YELLOW}安装过程中，如遇到询问选择，请输(y)，安装过程极易出错，请重试安装${RES}"
	sleep 2
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
	apt update && apt --fix-broken install -y && apt install qemu-system-x86-64-headless qemu-system-i386-headless -y
else
	apt update && apt install qemu-system-x86 xserver-xorg x11-utils samba -y && apt --reinstall install pulseaudio -y
	fi
        QEMU_SYSTEM
        ;;
2) echo -e "创建windows镜像目录及共享目录\n"
        if [ ! -e "/sdcard/xinhao/windows" ]; then
                mkdir -p /sdcard/xinhao/windows
        fi
        if [ ! -e "/sdcard/xinhao/share/" ]; then
                mkdir -p /sdcard/xinhao/share
        fi
        if [ ! -e "/sdcard/xinhao/windows" ]; then        
	echo -e "${RED}创建目录失败${RES}"
        else
                echo -e "${GREEN}手机根目录下已创建/xinhao/windows文件夹，请把系统镜像，分驱镜像，光盘放进这个目录里\n共享目录是/xinhao/share(目录内总文件大小不能超过500m)\n${RES}"
        fi
        CONFIRM
        QEMU_SYSTEM
        ;;
3) export PULSE_SERVER=tcp:127.0.0.1:4713
	read -r -p "请选择显示输出方式 1)vnc 2)xsdl(不推荐)" input
	case $input in
		1|"") echo -e "${YELLOW}vncviewer地址为127.0.0.1:0${RES}"
			sleep 1
			set -- "${@}" "-vnc" ":0" ;;
		2) echo "需先打开xsdl再继续此操作"
			sleep 1
			export DISPLAY=127.0.0.1:0 ;;
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
	echo -e "${GREEN}请确认系统镜像已放入手机目录/xinhao/windows里${RES}"
        CONFIRM
        pkill -9 qemu-system-x86
	pkill -9 qemu-system-i38
        qemu-system-x86_64 --version | grep ':5' -q || uname -a | grep 'Android' -q
				if [ $? != 0 ]; then
		case $(dpkg --print-architecture) in
                        arm*|aarch64) set -- "${@}" "--accel" "tcg,thread=multi" ;;
                *) set -- "${@}" "-machine" "pc,accel=kvm:xen:hax:tcg" ;;
esac
	else
		echo -e "请选择计算机类型，因系统原因，q35可能导致启动不成功"
		read -r -p "1)pc默认 2)q35" input
		case $input in
			1|"") case $(dpkg --print-architecture) in
			arm*|aarch64) set -- "${@}" "--accel" "tcg" ;;
                *) set -- "${@}" "-machine" "pc,accel=kvm:xen:hax:tcg" ;;
	esac ;;
			2) echo -e ${RED}"如果无法进入系统，请选择pc${RES}"
				set -- "${@}" "-machine" "q35,accel=kvm:xen:hax:tcg" ;;
		esac
#		set -- "${@}" "-machine" "q35"
		fi
echo -n -e "请输入${YELLOW}系统镜像${RES}全名（例如andows.img）hda_name:"
read hda_name
#qemu-system-x86_64 --version | grep ':5' -q || uname -a | grep 'Android' -q
#if [ $? != 0 ]; then
	echo -n -e "请输入${YELLOW}分区镜像${RES}全名,不加载请直接回车（例如hdb.img）hdb_name:"
	read hdb_name
#fi
echo -n -e "请输入${YELLOW}光盘${RES}全名,不加载请直接回车（例如DVD.iso）iso_name:"
read iso_name
#		set -- "${@}" "-net" "nic" "-net" "user,smb=/sdcard/xinhao/"
        echo -n "请输入模拟的内存大小，以m为单位（1g=1024m ， 例如512）mem:"
        read mem
        set -- "${@}" "-m" "$mem"
        set -- "${@}" "-rtc" "base=localtime"
	echo -e "是否自定义cpu数量"
	read -r -p "1)默认配置 2)自定义" input
	case $input in
		1|"") _SMP="" ;;
		2) CPU=0
			while [ $CPU -eq 0 ]
do
	echo -n -e "请输入逻辑cpu参数，分别为核心、线程、插槽个数，输入三位数字(例如2核1线2插槽,不能有0 则输212)"
	read SMP     
	CORES=`echo $SMP | cut -b 1`   
	THREADS=`echo $SMP | cut -b 2`    
	SOCKETS=`echo $SMP | cut -b 3`    
	let CPU=$CORES*$THREADS*$SOCKETS 2>/dev/null
done
echo -e "${YELLOW}$CORES核心$THREADS线程$SOCKETS插槽${RES}"
_SMP="$CPU,cores=$CORES,threads=$THREADS,sockets=$SOCKETS" ;;
esac
	read -r -p "请选择cpu 1)core2duo 2)athlon 3)pentium2 4)n270 5)Skylake-Server-IBRS" input
        case $input in
        1) set -- "${@}" "-cpu" "core2duo"
		if [ -n "$_SMP" ]; then
			set -- "${@}" "-smp" "$_SMP"
		else
                set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1"
		fi ;;
        2) set -- "${@}" "-cpu" "athlon"
		if [ -n "$_SMP" ]; then
			set -- "${@}" "-smp" "$_SMP"
		else
                set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1"
			fi ;;
        3) set -- "${@}" "-cpu" "pentium2"
		if [ -n "$_SMP" ]; then
			set -- "${@}" "-smp" "$_SMP"
		else
                set -- "${@}" "-smp" "1,cores=1,threads=1,sockets=1"
			fi ;;
        4) set -- "${@}" "-cpu" "n270"
		if [ -n "$_SMP" ]; then
			set -- "${@}" "-smp" "$_SMP"
		else
                set -- "${@}" "-smp" "2,cores=1,threads=2,sockets=1"
		fi ;;
	5) set -- "${@}" "-cpu" "Skylake-Server-IBRS" 
		if [ -n "$_SMP" ]; then
			set -- "${@}" "-smp" "$_SMP"
		else
		set -- "${@}" "-smp" "4,cores=2,threads=1,sockets=2"
			fi ;;
        *)      set -- "${@}" "-cpu" "max"
		if [ -n "$CPU" ]; then
                set -- "${@}" "-smp" "$CPU"
	else
		set -- "${@}" "-smp" "4"
		fi ;;
esac
read -r -p "请选择显卡 1)cirrus 2)vmware 3)std 4)virtio" input
        case $input in
                1) set -- "${@}" "-vga" "cirrus" ;;
                2) set -- "${@}" "-vga" "vmware" ;;
		3|"") set -- "${@}" "-vga" "std" ;;
		4) set -- "${@}" "-vga" "virtio" ;;
        esac
        read -r -p "请选择网卡 1)e1000 2)rtl8139 0)不加载" input
        case $input in
                        1|"") set -- "${@}" "-net" "user"
                                set -- "${@}" "-net" "nic,model=e1000" ;;
                        0) ;;
                        2) set -- "${@}" "-net" "user"
                                set -- "${@}" "-net" "nic,model=rtl8139" ;;
                esac
		read -r -p "是否加载usb鼠标,少部分系统可能不支持 1)加载 0)不加载" input
                case $input in
                        1) set -- "${@}" "-usb" "-device" "usb-tablet" ;;
                        2|"") ;;
                esac
		qemu-system-x86_64 --version | grep ':5' -q || uname -a | grep 'Android' -q
                if [ $? != 0 ]; then
			read -r -p "请选择声卡 1)ac97 2)sb16 3)es1370 4)hda 0)不加载" input
                        case $input in
                1|"") set -- "${@}" "-soundhw" "ac97" ;;
                2) set -- "${@}" "-soundhw" "sb16" ;;
                0) ;;
                3) set -- "${@}" "-soundhw" "es1370" ;;
		4) set -- "${@}" "-soundhw" "hda" ;;
esac
                set -- "${@}" "-hda" "/sdcard/xinhao/windows/$hda_name"
                if [ -n "$hdb_name" ]; then
                        set -- "${@}" "-hdb" "/sdcard/xinhao/windows/$hdb_name"
                fi
                if [ -n "$iso_name" ]; then
                        set -- "${@}" "-cdrom" "/sdcard/xinhao/windows/$iso_name"
                fi
                set -- "${@}" "-hdd" "fat:rw:/sdcard/xinhao/share/"
                set -- "${@}" "-boot" "order=dc"

        else
		read -r -p "请选择声卡 1)es1370 2)sb16 3)hda 4)ac97(推荐) 0)不加载" input
                        case $input in
                        1) set -- "${@}" "-device" "ES1370" ;;
                        2) set -- "${@}" "-device" "sb16" ;;
			3) set -- "${@}" "-device" "intel-hda" "-device" "hda-duplex" ;;
                        0) ;;
                        4|"") set -- "${@}" "-device" "AC97" ;;
                esac
		echo -e "请选择是否选择硬盘接口,因系统原因,sata可能导致启动不成功,virtio需系统已装驱动,回车为兼容方式"
		read -r -p "1)ide 2)sata 3)virtio " input
		case $input in
			1)
#STANDARD-INCORRECT
#		set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hda_name,if=ide,format=raw,index=0,media=disk"

#TRADITIONAL
#		set -- "${@}" "-hda" "/sdcard/xinhao/windows/$hda_name"

#STANDARD-CORRECT  
		set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hda_name,if=ide,index=0,media=disk"
#VIRTIO
#		set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hda_name,if=virtio,id=drive-virtio-disk,aio=threads,cache=none"
		if [ -n "$hdb_name" ]; then
#                       set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hdb_name,if=ide,format=raw,index=1,media=disk"
#                       set -- "${@}" "-hdb" "/sdcard/xinhao/windows/$hdb_name"                                           
		set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hdb_name,if=ide,index=1,media=disk"
		fi
		if [ -n "$iso_name" ]; then     
#                       set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$iso_name,if=ide,format=raw,index=2,media=cdrom"
#                       set -- "${@}" "-cdrom" "/sdcard/xinhao/windows/$iso_name"          
                       set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$iso_name,if=ide,index=2,media=cdrom"  
		fi                            
#               set -- "${@}" "-hdd" "fat:rw:/sdcard/xinhao/share/"                                                        
		set -- "${@}" "-drive" "file=fat:rw:/sdcard/xinhao/share,if=ide,index=3,media=disk" ;;


		2)
#		set -- "${@}" "-drive" "id=systemdisk,if=none,file=/sdcard/xinhao/windows/$hda_name" 
#		set -- "${@}" "-device" "ich9-ahci,id=sata"
#		set -- "${@}" "-device" "ide-hd,bus=sata.0,drive=systemdisk"

#SATA        
		set -- "${@}" "-drive" "id=disk,file=/sdcard/xinhao/windows/$hda_name,if=none"
		set -- "${@}" "-device" "ahci,id=ahci"
		set -- "${@}" "-device" "ide-hd,drive=disk,bus=ahci.0"

		if [ -n "$hdb_name" ]; then
#		set -- "${@}" "-drive" "id=ESP,if=none,file=/sdcard/xinhao/windows/$hdb_name"
#		set -- "${@}" "-device" "ide-hd,bus=sata.1,drive=ESP"
		set -- "${@}" "-drive" "id=installmedia,file=/sdcard/xinhao/windows/$hdb_name,if=none" 
		set -- "${@}" "-device" "ide-hd,drive=installmedia,bus=ahci.1"
		fi
		if [ -n "$iso_name" ]; then
#		set -- "${@}" "-drive" "id=cdrom,if=none,file=/sdcard/xinhao/windows/$iso_name"
#		set -- "${@}" "-device" "ide-cd,bus=sata.2,drive=cdrom"
		set -- "${@}" "-drive" "id=cdrom,file=/sdcard/xinhao/windows/$iso_name,if=none"     
		set -- "${@}" "-device" "ide-cd,drive=cdrom,bus=ahci.2"
		fi

#		set -- "${@}" "-drive" "id=InstallMedia,format=raw,if=none,file=fat:rw:/sdcard/xinhao/share/"
#		set -- "${@}" "-device" "ide-hd,bus=sata.3,drive=InstallMedia" 
		
#		set -- "${@}" "-drive" "if=none,format=raw,id=disk1,file=fat:rw:/sdcard/xinhao/share/"
#		set -- "${@}" "-device" "ich9-usb-ehci1,id=usb"
#		set -- "${@}" "-device" "usb-storage,bus=usb.0,drive=disk1"

		set -- "${@}" "-usb" "-drive" "if=none,format=raw,id=disk1,file=fat:rw:/sdcard/xinhao/share/"
#		set -- "${@}" "-machine" "usb=on"
		set -- "${@}" "-device" "usb-storage,drive=disk1"	;;
	3) set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hda_name,index=0,media=disk,if=virtio"
#	       	set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hda_name,if=virtio,id=drive-virtio-disk,aio=threads,cache=none"
if [ -n "$hdb_name" ]; then
			set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hdb_name,index=1,media=disk,if=virtio" 
		fi
		if [ -n "$iso_name" ]; then
			set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$iso_name,index=2,media=cdrom"
		fi
			set -- "${@}" "-drive" "file=fat:rw:/sdcard/xinhao/share,index=3,media=disk,if=virtio"
;;
		*) set -- "${@}" "-hda" "/sdcard/xinhao/windows/$hda_name" 
	if [ -n "$hdb_name" ]; then
		set -- "${@}" "-hdb" "/sdcard/xinhao/windows/$hdb_name"
		fi
		if [ -n "$iso_name" ]; then
			set -- "${@}" "-cdrom" "/sdcard/xinhao/windows/$iso_name"
			fi
			set -- "${@}" "-hdd" "fat:rw:/sdcard/xinhao/share/" ;;
esac
		set -- "${@}" "-boot" "order=dc,menu=on,strict=off"
		fi
        set -- "$QEMU_SYS" "${@}"
        "${@}" &
        ;;
4) WEB_SERVER ;;
5) exit 1 ;;
*) INVALID_INPUT && QEMU_SYSTEM ;;
esac
}
###################
MAIN() {
	PULSEAUDIO
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
	echo -e "\n请选择qemu-system-x86的运行方式\n
	1) 直接运行，termux(utermux)目前版本为5.0以上，暂不支持声音输出，其他系统的版本各不一样
	2) 独立系统运行5.0以上版本
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
else
	QEMU_SYSTEM
	fi
}
####################
MAIN "$@"
