#!/usr/bin/env bash

clear

##################
YELLOW="\e[1;33m"
GREEN="\e[1;32m"
RED="\e[1;31m"
BLUE="\e[1;34m"
PINK="\e[0;35m"
RES="\e[0m"
##################
echo -e "\n${GREEN}部分系统没相应的架构包，可能会导致下载不成功d(ŐдŐ๑)                                              
${PINK}手上资源有限，无法每个架构都通过测试(>﹏<)${RES}\n"   
read -r -p "按回车键继续" input                           
case $input in                                            
*) ;; esac
##################
ARCH_(){
	if [ "$(dpkg --print-architecture)" = "aarch64" ];then
		ARCH=arm64
		echo "aarch64"
        elif [ "$(dpkg --print-architecture)" = "arm64" ];then
		ARCH=arm64
		echo "arm64"
        elif [ "$(dpkg --print-architecture)" = "x86_64" ];then
		ARCH=amd64
		echo "x86_64"
        elif [ "$(dpkg --print-architecture)" = "x86" ];then
		ARCH=i386
		echo "x86"
        elif [ "$(dpkg --print-architecture)" = "amd64" ];then
		ARCH=amd64
		echo "amd64"
        elif [ "$(dpkg --print-architecture)" = "i686" ];then
		ARCH=i386
		echo "i686"
        elif [ "$(dpkg --print-architecture)" = "i386" ];then
		ARCH=i386
		echo "i386"
        elif [ "$(dpkg --print-architecture)" = "i586" ];then
		ARCH=i386
		echo "i586"
        else
            printf 'unknown architecture\n'
            exit 1
        fi
}
#####################
INVALID_INPUT() {
	echo -e "${RED}输入无效，请重输${RES}" \\n
	sleep 1
}
#####################
CONFIRM() {
	read -r -p "按回车键继续" input
	case $input in
		*) ;; esac
}
#####################
SYS_SELECT() {
	echo -e "\n\e[33m请选择系统\n
	1) debian(buster)\n
	2) ubuntu(bionic)\n
	3) ubuntu(focal)\n
	4) kali\n
	5) centos\n
	6) arch\n
	7) XINHAO_HAN 先占个位\n
	9) 返回主目录\n 
	0) 退出${RES}"
read -r -p ":" input
case $input in
	1) echo "即将下载安装debian(buster)"
		sys_name=buster
		DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/debian/buster/$ARCH/default/" ;;
	2) echo "即将下载安装ubuntu(bionic)"
		sys_name=bionic
		DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/ubuntu/bionic/$ARCH/default/" ;;
	3) echo "即将下载安装ubuntu(focal)"
		sys_name=focal
		DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/ubuntu/focal/$ARCH/default/" ;;
	4) echo "即将下载安装kali"
                sys_name=kali
		DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/kali/current/$ARCH/default/" ;;
	5) echo "即将下载安装centos"
                sys_name=centos
		DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/centos/8/$ARCH/default/" ;;
	6) echo "即将下载安装arch"
                sys_name=arch
		DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/archlinux/current/$ARCH/default/" ;;
	7) echo "别乱选"
		SYS_SELECT
                #sys_name=alpine
		#DEF_CUR="https://mirrors.bfsu.edu.cn/lxc-images/images/alpine/3.10/arm64/default/"
		;;
	9) MAIN ;;
	0) echo -e "\nexit..."
		sleep 1
		exit 0 ;;

	*) INVALID_INPUT
		SYS_SELECT ;;
esac
}
#####################
SYS_SELECT_() {
        echo -e "\n\e[33m请选择系统\n
        1) debian(buster)\n
        2) ubuntu(bionic)\n
        3) ubuntu(focal)\n
        4) kali\n
        5) centos\n
        6) arch\n
        7) XINHAO_HAN 先占个位\n
        9) 返回主目录\n
	0) 退出${RES}"
read -r -p ":" input
case $input in
        1) echo "即将下载安装debian(buster)"
                sys_name=buster
                DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/debian/buster/$ARCH/default/" ;;
        2) echo "即将下载安装ubuntu(bionic)"
                sys_name=bionic
                DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/ubuntu/bionic/$ARCH/default/" ;;
        3) echo "即将下载安装ubuntu(focal)"
                sys_name=focal
                DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/ubuntu/focal/$ARCH/default/" ;;
        4) echo "即将下载安装kali"
                sys_name=kali
                DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/kali/current/$ARCH/default/" ;;
        5) echo "即将下载安装centos"
                sys_name=centos
                DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/centos/8/$ARCH/default/" ;;
        6) echo "即将下载安装arch"
                sys_name=arch
                DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/archlinux/current/$ARCH/default/" ;;
        7) echo "别乱选"
                SYS_SELECT
                #sys_name=alpine
                #DEF_CUR="https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/alpine/3.10/arm64/default/"
                ;;
        9) MAIN ;;
	0) echo -e "\nexit..."
                sleep 1
                exit 0 ;;

        *) INVALID_INPUT
                SYS_SELECT ;;
esac
}

#####################

SYS_DOWN() {
BAGNAME="rootfs.tar.xz"
        if [ -e ${BAGNAME} ]; then
                rm -rf ${BAGNAME}
	fi
	curl -o ${BAGNAME} ${DEF_CUR}
		VERSION=`cat ${BAGNAME} | grep href | tail -n 2 | cut -d '"' -f 4 | head -n 1`
		curl -o ${BAGNAME} ${DEF_CUR}${VERSION}${BAGNAME}
		if [ $? -ne 0 ]; then
			echo -e "${RED}下载失败，请重输${RES}\n"
			MAIN
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
}
####################
SYS_SET() {
	echo "更新DNS"
	echo "127.0.0.1 localhost" > $sys_name/etc/hosts
	rm -rf $sys_name/etc/resolv.conf &&
	echo "nameserver 114.114.114.114
nameserver 8.8.4.4" >$sys_name/etc/resolv.conf
	echo "export  TZ='Asia/Shanghai'" >> $sys_name/root/.bashrc
	if grep -q 'ubuntu' "$sys_name/etc/os-release" ; then
        touch "$sys_name/root/.hushlogin"
fi
}
####################
FIN(){
echo "写入启动脚本"
cat > $sys_name.sh <<- EOM
#!/bin/bash
cd $(dirname $0)
## unset LD_PRELOAD in case termux-exec is installed
pulseaudio --start &
echo "" &
echo "欢迎来到$sys_name系统" &
unset LD_PRELOAD
command="proot"
command+=" --kill-on-exit"
command+=" --link2symlink"
command+=" -S $sys_name"
command+=""
command+=" -b $sys_name/root:/dev/shm"
## uncomment the following line to have access to the home directory of termux
#command+=" -b /data/data/com.termux/files/home:/root"
## uncomment the following line to mount /sdcard directly to /
command+=" -b /sdcard"
command+=" -w /root"
command+=" /usr/bin/env -i"
command+=" HOME=/root"
command+=" USER=root"
command+=" PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games"
command+=" TERM=$TERM"
command+=" LANG=C.UTF-8"
command+=" /bin/bash --login"
com="\$@"
if [ -z "\$1" ];then
    exec \$command
else
    \$command -c "\$com"
fi
EOM

echo "授予 $sys_name.sh 执行权限"
chmod +x $sys_name.sh
if [ -e ${PREFIX}/etc/bash.bashrc ]; then
	if ! grep -q 'pulseaudio' ${PREFIX}/etc/bash.bashrc; then
		sed -i "1i\pkill -9 pulseaudio" ${PREFIX}/etc/bash.bashrc
	fi
else
	sed -i "1i\pkill -9 pulseaudio" $sys_name.sh
fi
echo -e "${YELLOW}现在可以执行 ./$sys_name.sh 运行 $sys_name 了${RES}"
}
####################
MAIN(){
	printf "\n检测到你的CPU架构是"
	ARCH_
	echo -e "\n${YELLOW}请选择系统下载地址(推荐北京外国语大学)\n
	1) 北京外国语大学
	2) 清华大学
	0) 退出${RES}\n"
	read -r -p "请选择:" input
	case $input in
		1) SYS_SELECT ;;
		2) SYS_SELECT_ ;;
		0) exit 1 ;;
		*) echo -e "${RED}无效选择，请重选${RES}"
			sleep 2
			MAIN ;;
	esac
	SYS_DOWN
	SYS_SET
	FIN
}
####################
MAIN "$@"
