#!/data/data/com.termux/files/usr/bin/bash
cd $(dirname $0)
####################
INFO() {
clear
echo -e "\n\e[33m更新内容\e[0m
	增加spice视频输出选项，注意：spice一旦断开连接，再次连上后，鼠键不能用（不支持termux与utermux环境）
	增加qxl显卡选项，需安装virtio驱动才能发挥特性功能
	增加vietio显卡选项，3D功能由于参数问题，只能在电脑上使用，未经测试，具体信息请查阅关于utqemu。
	增加了一些未经完全测试通过的参数配置
	修改了一些细节\n
\e[33m注意事项\e[0m		
	本脚本是方便大家简易配置，所有参数都是经多次测试通过，可运行大部分系统，由于兼容问题，性能不作保证，专业玩家请自行操作
	qemu5.0以上版本较旧版本多主板q35，硬盘接口的选项
	q35主板与sata，virtio硬盘接口由于系统原因，可能导致启动不成功
	如遇到使用异常，请尝试所有选择项直接回车以获得默认参数
	运行速度不稳定，受termux(utermux)环境影响，偶尔模拟出来的运行速度极慢
	声音输出（不支持termux与utermux环境）
	sdl输出显示，需先开启xsdl(不支持termux与utermux环境）
	qemu5.0以下模拟xp较好，qemu5.0以上对win7以上模拟较好\n"
	if [ $(command -v qemu-system-x86_64) ]; then
		echo -e "\e[33m检测到你已安装qemu-system-x86，版本是\e[0m"
		qemu-system-x86_64 --version | head -n 1
	fi
}
###################
ABOUT_VIRTIO(){
	clear
printf "%s
${YELLOW}关于utqemu脚本${RES}
	最初是为utermux写下的qemu-system-x86脚本，目的是增加utermux可选功能，给使用者提供简易快捷的启动。为适配常用镜像格式，脚本的参数选用是比较常用。我是业余的，专业的参数配置并不懂，脚本参数都是来自官方网站、百度与群友。qemu5.0以上的版本较旧版本变化比较大，所以5.0后的参数选项比较丰富，欢迎群友体验使用。

${YELLOW}关于virtio驱动${RES}
	引用官方说法：QEMU为用户提供并行虚拟化块设备和网络设备的能力，其是借助virtio驱动实现的，拥有更好的性能表现以及更低的开销。

${YELLOW}virtio驱动的安装${RES}
	需下载好virtio驱动光盘，virtio磁盘接口安装程序比较多，其他驱动与普通的硬件驱动一样安装，本脚本已加入qxl显卡，virtio显卡，virtio网卡，virtio磁盘选项。

${YELLOW}关于virtio显卡3D加速${RES}
	virtio显卡因参数问题，未发挥其特性功能。经过多次测试，作出的参数配置如下，当你选择virtio显卡时，vnc，sdl，spice输出不再有效，但仍会按你的上述选择作出以下配置。sdl将以-display sdl,gl=on输出（因系统的qemu源默认未编译sdl内容，所以选项未得到测试验证）。而spice则以wiki上的标准参数-display gtk,gl=on输出，但virtio显卡并不被识别。所以，vnc除了spice上的参数外，我还加入了-vga qxl来兼容virtio显卡输出。至于效果如何，有待验证。

${YELLOW}系统镜像的磁盘驱动安装介绍：${RES}
	1)先创建一个新的磁盘镜像，用于搜索virtio驱动，参数如下
qemu-img create -f qcow2 fake.qcow2 1G
	2)挂载fake磁盘（处于virtio模式下），带有驱动的CD-ROM，运行原本的Windows客户机（boot磁盘依旧是处于IDE模式中），参数如下
qemu-system-x86_64 -m 4G -drive file=系统镜像,if=ide -drive file=fake.qcow2,if=virtio -cdrom virtio驱动.iso
	3)开机Windows会自动检测fake磁盘，并搜索适配的驱动。如果失败了，前往Device Manager，找到SCSI驱动器（带有感叹号图标，应处于打开状态），点击Update driver并选择虚拟的CD-ROM。不要定位到CD-ROM内的文件夹了，只选择CD-ROM设备就行，Windows会自动找到合适的驱动的。
	4)关机并重新启动它，现在可以以virtio模式挂载boot磁盘
qemu-system-x86_64 -m 4G -drive file=系统镜像,if=virtio

"
CONFIRM
MAIN
}
####################

YELLOW="\e[1;33m"
GREEN="\e[1;32m"
RED="\e[1;31m" 
BLUE="\e[1;34m"
PINK="\e[0;35m"  
WHITE="\e[0;37m"
RES="\e[0m"
####################
ARCH_CHECK() {
case $(dpkg --print-architecture) in
	arm*|aarch64|i*86) ;;
	*) echo -e "${RED}不支持你设备的架构${RES}" ;;
esac
}
#################
LOGIN() {
pulseaudio --start & 2>/dev/null
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
SYSTEM_CHECK() {
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
		grep -E 'bfsu|tsinghua|ustc|tencent|utqemucheck' ${PREFIX}/etc/apt/sources.list              
		if [ $? != 0 ]; then  
			echo -e "${YELLOW}检测到你使用的可能为非国内源，为保证正常使用，建议切换为国内源${RES}\n  
			1) 换国内源    
			2) 不换"   
			read -r -p "是否换国内源:" input   
			case $input in    
				1|"") echo "换国内源" 
sed -i 's@^\(deb.*stable main\)$@#\1\ndeb https://mirrors.bfsu.edu.cn/termux/termux-packages-24 stable main@' $PREFIX/etc/apt/sources.list 
sed -i 's@^\(deb.*games stable\)$@#\1\ndeb https://mirrors.bfsu.edu.cn/termux/game-packages-24 games stable@' $PREFIX/etc/apt/sources.list.d/game.list 
sed -i 's@^\(deb.*science stable\)$@#\1\ndeb https://mirrors.bfsu.edu.cn/termux/science-packages-24 science stable@' $PREFIX/etc/apt/sources.list.d/science.list && pkg update -n ;;  
2) echo "#utqemucheck" >>${PREFIX}/etc/apt/sources.list
	MAIN ;;  
esac                                                    
		fi
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
if [ ! $(command -v proot) ]; then
	apt update && apt install proot -y
fi
	fi
}
##################
WEB_SERVER() {
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
		if [ ! $(command -v python) ]; then
			echo -e "\n检测到你未安装所需要的包python,将先为你安装上"
			apt update && apt install python -y
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
1) 安装qemu-system-x86_64，并联动更新模拟器所需应用\n\e[33m(由于qemu的依赖问题，安装过程可能会失败，请尝试重新安装)${RES}
2) 创建windows镜像目录
3) 启动qemu-system-x86_64模拟器
4) 让termux成为网页服务器(使模拟系统可以通过浏览器访问手机内容)
5) 关于utqemu
0) 退出\n"
read -r -p "请选择:" input
case $input in
	1)  echo -e "${YELLOW}安装过程中，如遇到询问选择，请输(y)，安装过程极易出错，请重试安装${RES}"
	sleep 2
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
	apt update && apt --fix-broken install -y && apt install qemu-system-x86-64-headless qemu-system-i386-headless -y
else
	apt update && apt install qemu-system-x86 xserver-xorg x11-utils -y && apt --reinstall install pulseaudio -y
#apt install samba
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
3) if [ ! $(command -v qemu-system-x86_64) ]; then
	echo -e "\n${GREEN}检测到你未安装qemu，请先执行安装选项${RES}"
	sleep 2
	QEMU_SYSTEM
fi
	uname -a | grep 'Android' -q 
	if [ $? == 0 ]; then
		echo -e "${YELLOW}vncviewer地址为127.0.0.1:0${RES}"     
		sleep 1     
		export PULSE_SERVER=tcp:127.0.0.1:4713  
		set -- "${@}" "-vnc" ":0"
	else
cat >/dev/null<<-'EOF'
ports=$(netstat | grep localhost | cut -d ":" -f 2 | cut -d " " -f 1 | head -n 1 )
i=5900
while [ "$i" == "$ports" ]
do
sleep 0.1
let i++
done
echo "$i"
EOF
	read -r -p "请选择显示输出方式 1)vnc 2)xsdl 3)spice " input
	case $input in
		1|"") echo -e "${YELLOW}vncviewer地址为127.0.0.1:0${RES}"
			sleep 1
			display=vnc
			export PULSE_SERVER=tcp:127.0.0.1:4713
#			set -- "${@}" "-vnc" ":0"
			;;
		2) echo "需先打开xsdl再继续此操作"
			sleep 1
			display=xsdl
			export PULSE_SERVER=tcp:127.0.0.1:4713
#			export DISPLAY=127.0.0.1:0 
			;;
		3) echo -e "${YELLOW}aspice地址为127.0.0.1:0${RES}
\e[33m请勿随意切换aspice，如出现系统界面无法控制，只能重开qemu${RES}"
sleep 1
display=spice
#			set -- "${@}" "-spice" "port=5900,addr=127.0.0.1,disable-ticketing,seamless-migration=on"
;;
	esac
	fi
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
		read -r -p "1)pc默认 2)q35 " input
		case $input in
			1|"") case $(dpkg --print-architecture) in
			arm*|aarch64) set -- "${@}" "--accel" "tcg" ;;
                *) set -- "${@}" "-machine" "pc,accel=kvm:xen:hax:tcg" ;;
	esac ;;
			2) echo -e ${RED}"如果无法进入系统，请选择pc${RES}"
				set -- "${@}" "-machine" "q35,accel=kvm:xen:hax:tcg" ;;
		esac
		fi
echo -n -e "请输入${YELLOW}系统镜像${RES}全名（例如andows.img）hda_name:"
read hda_name
	echo -n -e "请输入${YELLOW}分区镜像${RES}全名,不加载请直接回车（例如hdb.img）hdb_name:"
	read hdb_name
echo -n -e "请输入${YELLOW}光盘${RES}全名,不加载请直接回车（例如DVD.iso）iso_name:"
read iso_name
#		set -- "${@}" "-net" "nic" "-net" "user,smb=/sdcard/xinhao/"
        echo -n "请输入模拟的内存大小，以m为单位（1g=1024m，例如512）mem:"
        read mem
        set -- "${@}" "-m" "$mem"
        set -- "${@}" "-rtc" "base=localtime"
	set -- "${@}" "-nodefaults"
	echo -e "是否自定义cpu数量"
	read -r -p "1)默认配置 2)自定义 " input
	case $input in
		1|"") _SMP="" ;;
		2) CPU=0
			while [ $CPU -eq 0 ]
do
	echo -n -e "请输入逻辑cpu参数，分别为核心、线程、插槽个数，输入三位数字(例如2核1线2插槽,不能有0 则输212) "
	read SMP     
	CORES=`echo $SMP | cut -b 1`   
	THREADS=`echo $SMP | cut -b 2`    
	SOCKETS=`echo $SMP | cut -b 3`    
	let CPU=$CORES*$THREADS*$SOCKETS 2>/dev/null
done
echo -e "${YELLOW}$CORES核心$THREADS线程$SOCKETS插槽${RES}"
_SMP="$CPU,cores=$CORES,threads=$THREADS,sockets=$SOCKETS" ;;
esac
read -r -p "请选择cpu 1)core2duo 2)athlon 3)pentium2 4)n270 5)Skylake-Server-IBRS 6)Nehalem-IBRS " input
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
	6) set -- "${@}" "-cpu" "Nehalem-IBRS"
		if [ -n "$_SMP" ]; then
			set -- "${@}" "-smp" "$_SMP"
		else
			set -- "${@}" "-smp" "8,cores=8,threads=1,sockets=1"
			fi ;;
        *)      set -- "${@}" "-cpu" "max"
		if [ -n "$CPU" ]; then
                set -- "${@}" "-smp" "$CPU"
	else
		set -- "${@}" "-smp" "4"
		fi ;;
esac
	uname -a | grep 'Android' -q 
	if [ $? == 0 ]; then
read -r -p "请选择显卡 1)cirrus 2)vmware 3)std 4)virtio " input
	case $input in 
		1) set -- "${@}" "-vga" "cirrus" ;;
		2) read -r -p "1)不开启3D功能 2)开启3D功能 " input
			case $input in
				1|"") set -- "${@}" "-vga" "vmware"     ;;
				2) set -- "${@}" "-device" "vmware-svga,vgamem_mb=256" ;;
			esac ;;
		3|"") set -- "${@}" "-vga" "std" ;; 
		4) set -- "${@}" "-vga" "virtio" ;;
        esac
else
	read -r -p "请选择显卡 1)cirrus 2)vmware 3)std 4)virtio 5)qxl " input
        case $input in
                1) set -- "${@}" "-vga" "cirrus" ;;
                2) read -r -p "1)不开启3D功能 2)开启3D功能 " input
			case $input in
				1|"") set -- "${@}" "-vga" "vmware"     ;;
				2) set -- "${@}" "-device" "vmware-svga,vgamem_mb=256" ;;
			esac ;;
		3|"") set -- "${@}" "-vga" "std" ;;
		4) 
			echo -e "${YELLOW}virtio显卡带3D功能，但因使用的系统环境原因，目前只能通过电脑启用，如果真想尝试，可在图形界面打开(需32位色彩，否则出现花屏)。${RES}"
			read -r -p "1)不开启3D功能 2)开启3D功能 " input 
case $input in
		1|"") set -- "${@}" "-vga" "virtio" ;;
		2) 
			case $display in
			xsdl) set -- "${@}" "-vga" "virtio" "-display" "sdl,gl=on" ;;
			vnc) set -- "${@}" "-vga" "qxl" "-display" "gtk,gl=on" "-device" "virtio-gpu-pci,virgl=on" ;;
#				    set -- "${@}" "-vga" "virtio" "-display" "gtk,gl=on" ;;
			spice) set -- "${@}" "-vga" "virtio" "-display" "gtk,gl=on" ;;
		esac 
		unset display ;;
esac ;;


		5) case $display in
			spice) read -r -p "1)常规使用 2)spice传输协议使用 " input
			case $input in
			1|"") set -- "${@}" "-vga" "qxl" ;;
		2) set -- "${@}" "-vga" "qxl" "-device" "virtio-serial-pci" "-device" "virtserialport,chardev=spicechannel0,name=com.redhat.spice.0" "-chardev" "spicevmc,id=spicechannel0,name=vdagent" ;;
        esac ;;

*) set -- "${@}" "-vga" "qxl" ;;
esac
esac
	fi
	read -r -p "请选择网卡 1)e1000 2)rtl8139 3)virtio 0)不加载 " input
        case $input in
                        1|"") set -- "${@}" "-net" "user"
                                set -- "${@}" "-net" "nic,model=e1000" ;;
                        0) ;;
                        2) set -- "${@}" "-net" "user"
                                set -- "${@}" "-net" "nic,model=rtl8139" ;;
			3) set -- "${@}" "-net" "user"
				set -- "${@}" "-net" "nic,model=virtio" ;;
                esac
		read -r -p "是否加载usb鼠标,少部分系统可能不支持 1)加载 0)不加载 " input
                case $input in
                        1) set -- "${@}" "-usb" "-device" "usb-tablet" ;;
                        2|"") ;;
                esac
		qemu-system-x86_64 --version | grep ':5' -q || uname -a | grep 'Android' -q
                if [ $? != 0 ]; then
			set -- "${@}" "-realtime" "mlock=off"
			read -r -p "请选择声卡 1)ac97 2)sb16 3)es1370 4)hda 0)不加载 " input
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
		set -- "${@}" "-overcommit" "mem-lock=off"
		set -- "${@}" "-overcommit" "cpu-pm=off"
		read -r -p "请选择声卡 1)es1370 2)sb16 3)hda 4)ac97(推荐) 0)不加载 " input
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
#set -- "${@}" "-drive" "file=/sdcard/xinhao/windows/$hdb_name,if=virtio"
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

		if [ -n "$display" ]; then
			case $display in
				vnc) set -- "${@}" "-vnc" ":0" ;;
				xsdl) export DISPLAY=127.0.0.1:0 ;;
				spice) set -- "${@}" "-spice" "port=5900,addr=127.0.0.1,disable-ticketing,seamless-migration=on" ;;
			esac
		fi



        set -- "$QEMU_SYS" "${@}"
        "${@}" &
        ;;
4) WEB_SERVER ;;
5) ABOUT_VIRTIO ;;
0) exit 1 ;;
*) INVALID_INPUT && QEMU_SYSTEM ;;
esac
}
###################
MAIN() {
	ARCH_CHECK
	SYSTEM_CHECK
	INFO
	uname -a | grep 'Android' -q
	if [ $? == 0 ]; then
	echo -e "\n\e[33m请选择qemu-system-x86的运行环境\e[0m\n
	1) 直接运行，termux(utermux)目前版本为5.0以上，暂不支持声音输出，\e[33m其他系统的版本各不一样，一些功能参数可能没被编译进去${RES}
	2) 独立系统运行5.0以上版本
	0) 退出\n"
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
	0) exit 1 ;;
	*) INVALID_INPUT
		MAIN ;;
esac
else
	QEMU_SYSTEM
	fi
}
####################
MAIN "$@"
