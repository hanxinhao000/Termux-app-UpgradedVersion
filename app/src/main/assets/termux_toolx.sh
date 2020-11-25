#!/usr/bin/env bash
#####################
clear
#uname -a
#free
#######################

YELLOW_COLOR="\e[1;33m"
GREEN_COLOR="\e[1;32m"
RED_COLOR="\e[1;31m"
BLUE_COLOR="\e[1;34m"
PINK_COLOR="\e[0;35m"
RES="\e[0m"
echo -e "${BLUE_COLOR}welcome to use termuux-toolx!\n${RES}"
echo -e "这个脚本是方便使用者自定义安装设置\n包括系统包也是很干净的"
if [ `whoami` == "root" ];then
	echo -e "${BLUE_COLOR}当前用户为root${RES}"
else
	echo -e "${RED_COLOR}当前用户为$(whoami)，建议切换root用户${RES}\n"
	sleep 1

fi
if [ ! -e "/etc/os-release" ]; then
printf '???'
echo -e "${GREEN_COLOR}"
cat <<'EOF'

          +hydNNNNdyh+
        +mMMMMMMMMMMMMm+
      `dMMm:NMMMMMMN:mMMd`
      hMMMMMMMMMMMMMMMMMMh
  ..  yyyyyyyyyyyyyyyyyyyy  ..
.mMMm`MMMMMMMMMMMMMMMMMMMM`mMMm.
:MMMM-MMMMMMMMMMMMMMMMMMMM-MMMM:
:MMMM-MMMMMMMMMMMMMMMMMMMM-MMMM:
:MMMM-MMMMMMMMMMMMMMMMMMMM-MMMM:
:MMMM-MMMMMMMMMMMMMMMMMMMM-MMMM:
-MMMM-MMMMMMMMMMMMMMMMMMMM-MMMM-
 +yy+ MMMMMMMMMMMMMMMMMMMM +yy+
      mMMMMMMMMMMMMMMMMMMm
      `/++MMMMh++hMMMM++/`
          MMMMo  oMMMM
          MMMMo  oMMMM
          oNMm-  -mMNs
EOF
echo -e "${RES}"
elif grep -q 'ID=debian' "/etc/os-release"; then
	printf '你的系统是'
cat /etc/os-release | grep PRETTY | cut -d '"' -f 2
echo -e "\e[0;31m"
cat <<'EOF'

	_,met$$$$$gg.         
    ,g$$$$$$$$$$$$$$$P.       
  ,g$$P"     """Y$$.".        
 ,$$P'              `$$$.     
',$$P       ,ggs.     `$$b:   
`d$$'     ,$P"'   .    $$$    
 $$P      d$'     ,    $$P    
 $$:      $$.   -    ,d$$'    
 $$;      Y$b._   _,d$P'      
 Y$$.    `.`"Y$$$$P"'         
 `$$b      "-.__
  `Y$$
   `Y$$.
     `$$b.
       `Y$$b.
          `"Y$b._
              `"""

EOF
echo -e "${RED_COLOR}"
elif grep -q 'ID=ubuntu' "/etc/os-release"; then
printf "你的系统是"
cat /etc/os-release | grep PRETTY | cut -d '"' -f 2
	echo -e "${RED_COLOR}"
cat <<'EOF'

	    .-/+oossssoo+/-.
        `:+ssssssssssssssssss+:`
      -+ssssssssssssssssssyyssss+-
    .ossssssssssssssssssdMMMNysssso.
   /ssssssssssshdmmNNmmyNMMMMhssssss/
  +ssssssssshmydMMMMMMMNddddyssssssss+
 /sssssssshNMMMyhhyyyyhmNMMMNhssssssss/
.ssssssssdMMMNhsssssssssshNMMMdssssssss.
+sssshhhyNMMNyssssssssssssyNMMMysssssss+
ossyNMMMNyMMhsssssssssssssshmmmhssssssso
ossyNMMMNyMMhsssssssssssssshmmmhssssssso
+sssshhhyNMMNyssssssssssssyNMMMysssssss+
.ssssssssdMMMNhsssssssssshNMMMdssssssss.
 /sssssssshNMMMyhhyyyyhdNMMMNhssssssss/
  +sssssssssdmydMMMMMMMMddddyssssssss+
   /ssssssssssshdmNNNNmyNMMMMhssssss/
    .ossssssssssssssssssdMMMNysssso.
      -+sssssssssssssssssyyyssss+-
        `:+ssssssssssssssssss+:`
            .-/+oossssoo+/-.
EOF
echo -e "${RES}"
fi
#####################
CHECK (){
	if [ -e /etc/os-release ]; then
        printf '你的系统是'
        cat /etc/os-release | head -n 1 | cut -d '"' -f 2
else
		echo -e "${RED_COLOR}你用的不是Debian或Ubuntu系统，操作将中止...${RES}"
	sleep 2
        MAIN
	fi
if grep -q 'ID=debian' "/etc/os-release"; then
	echo ""
elif grep -q 'ID=ubuntu' "/etc/os-release"; then
	echo ""
else
	echo -e "${RED_COLOR}你用的不是Debian或Ubuntu系统，操作将中止...${RES}"
	sleep 2
	MAIN
fi
}
#####################
INVALID_INPUT() {
echo -e "${RED_COLOR}Invalid input...${RES}" \\n
echo "choose again"
sleep 1
}
#####################
CONFIRM() {
read -r -p "按回车键继续" input
case $input in
*) ;; esac
}
#####################
SETTLE() {
	echo -e "\n${BLUE_COLOR}1) 遇到关于Sub-process /usr/bin/dpkg returned an error code (1)错误提示
2) 安装个小火车(命令sl)
3) 增加普通用户并赋予sudo功能
4) 忽略Ubuntu出现的groups: cannot find name for group *提示
5) 设置时区
6) 安装进程树(可查看进程，命令pstree)
7) 安装网络信息查询(命令ifconfig)
8) 修改国内源地址sources.list(only for debian and ubuntu)
9) 修改dns
10) GitHub资源库(只支持debian和ubuntu)
11) python3和pip应用
12) 中文汉化
13) 安装系统信息显示(neofetch,screenfetch)${RES}"
read -r -p "1) 2) 3) 4) 5) 6) 7) 8) 9) 10) 11) 12) 13);E(Exit); M(Main)" input

case $input in
	1)
		echo "修复中..."
		sleep 1
		mv /var/lib/dpkg/info/ /var/lib/dpkg/info_old/&&mkdir /var/lib/dpkg/info/&&apt-get update&&apt-get -f install&&mv /var/lib/dpkg/info/* /var/lib/dpkg/info_old/&&mv /var/lib/dpkg/info /var/lib/dpkg/info_back&&mv /var/lib/dpkg/info_old/ /var/lib/dpkg/info
		echo "done"
		SETTLE
		;;
	2)
		echo "安装个小火车，运行命令sl"
		apt install sl && cp /usr/games/sl /usr/local/bin && sl 
		SETTLE
		;;
	3)
		echo -n "请输入普通用户名name:"
		read name
		if grep -q "$name" "/etc/passwd"; then
			echo -e "${RED_COLOR}你的普通用户名貌似已经有了，起个其他名字吧${RES}"
			sleep 2
			if [ ! -e /usr/bin/sudo ]; then
echo -e "${BLUE_COLOR}先帮你把这个用户的sudo功能装上。${RES}"
sleep 2
       	apt --reinstall install sudo
else
	read -r -p "sudo是否能用，如不能用请选择重新安装

1)重新安装 2)不需要重新安装" input
	case $input in
		1) apt --reinstall install sudo ;;
		2|"") echo "" ;;
	esac
#	chmod +4755 /usr/bin/sudo ; chown root:root /usr/bin/sudo ; chmod +w /etc/sudoers
			fi
	if grep -q "$name" "/etc/sudoers"; then
		echo ""
	else
	sed -i "/^root/a\\$name ALL=(ALL:ALL) ALL" /etc/sudoers
	fi
	echo "done"
else
		adduser $name
		if [ ! -e /usr/bin/sudo ]; then
			echo -e "${BLUE_COLOR}安装sudo${RES}"
			apt --reinstall install sudo
		fi
#		chmod +4755 /usr/bin/sudo && chown root:root /usr/bin/sudo && chmod +w /etc/sudoers
		sed -i "/^root/a\\$name ALL=(ALL:ALL) ALL" /etc/sudoers
		echo "done"
fi
echo "是否修改sudo临时生效时间，默认5分钟"
read -r -p "1)自定义时间 2)不修改" input
case $input in
	1) echo -n "请输入时间数字，以分钟为单位(例如20)sudo_time:"
		read sudo_time
		if grep -q 'timestamp' "/etc/sudoers"; then
			timestamp=`cat /etc/sudoers | grep timestamp | cut -d "=" -f 2`
			sed -i "s/env_reset,timestamp_timeout=$timestamp/env_reset,timestamp_timeout=$sudo_time/g" /etc/sudoers
		else
		sed -i "s/env_reset/env_reset,timestamp_timeout=$sudo_time/g" /etc/sudoers
	fi
	;;
*|"")
	echo ""
	;;
esac
		SETTLE
;;
4)
	echo "fix…"
	sleep 1
	touch ${HOME}/.hushlogin
	echo "done"
	sleep 1
	SETTLE
	;;
	[eE])
		echo "exit"
		exit 1
		;;
	[Mm])
                echo "back to main"
		MAIN
		;;
	5)
		echo -e "${GREEN_COLOR}请按界面提示选择 4) Asia;-> 9) China;-> 1) Beijing Time${RES}"
		echo "按回车键继续"
		read -r -p "" input
		case $input in
	*)
		tzselect
		sed -i "/^export TZ=/d" /etc/profile
		sed -i "1i\export TZ='Asia/Shanghai'" /etc/profile
		echo "done"
		sleep 2 ;;
esac
MAIN
;;
6)
	echo "安装进程树"
	apt install psmisc
	echo -e "${BLUE_COLOR}done${RES}"
	SETTLE
                ;;
	7)
		echo "安装ifconfig"
		apt install net-tools
		echo -e "${BLUE_COLOR}done${RES}"
		SETTLE
		;;
	8) SOURCES.LIST ;;
	9) MODIFY_DNS ;;
	10) ADD_GITHUB ;;
	11) INSTALL_PYTHON3 ;;
	12) LANGUAGE_CHANGE ;;
	13) echo -e "请选择安装\n1)neofetch； 2)screenfetch"
		read -r -p "1) 2) E(exit); M(main)" input
		case $input in
			1) echo "安装neofetch"
				apt install neofetch
				echo -e "${BLUE_COLOR}done${RES}"
				SETTLE ;;
			2) echo "安装screenfetch"
				apt install screenfetch
				echo -e "${BLUE_COLOR}done${RES}"
				SETTLE ;;
			[Ee]) echo "exit"
				exit 0 ;;
			[Mm]) echo "返回"
				SETTLE
				;;
			*) INVALID_INPUT
				SETTLE ;;
		esac ;;
	*) INVALID_INPUT
		SETTLE ;;
esac
}
################
LANGUAGE_CHANGE(){
                        echo "1)修改为中文; 2)修改为英文"
			read -r -p "1) 2)" input
			case $input in
			1) export LANGUAGE=zh_CN.UTF-8 && sed -i '/^export LANGUAGE/d' /etc/profile && sed -i '1i\export LANGUAGE=zh_CN.UTF-8' /etc/profile && source /etc/profile && echo '修改完毕,请重新登录' && sleep 2 && SETTLE ;;
2) export LANGUAGE=C.UTF-8 && sed -i '/^export LANGUAGE/d' /etc/profile && source /etc/profile && echo '修改完毕，请重新登录' && sleep 2 && SETTLE ;;
*) INVALID_INPUT
LANGUAGE_CHANGE ;;
esac
}
#################
SOURCES() {
SOURCES_ADDR="deb https://mirrors.ustc.edu.cn/debian"
SOURCES_SEC="deb https://mirrors.ustc.edu.cn/debian-security/"
DEB="main contrib non-free"
if [ -e /etc/os-release ]; then
        printf '你的系统是'
	cat /etc/os-release | head -n 1 | cut -d '"' -f 2
else
	echo -e "${RED_COLOR}你用的不是Debian或Ubuntu系统，操作将中止...${RES}"
	sleep 2
	MAIN
fi
if grep -q 'ID=debian' "/etc/os-release"; then
	echo ""
elif grep -q 'ID=ubuntu' "/etc/os-release"; then
	echo ""
else                                                     
		echo -e "${RED_COLOR}你用的不是Debian或Ubuntu系统，操作将中止...${RES}"                                                            
sleep 2
MAIN                                                     
fi

if grep -q 'ubuntu' "/etc/os-release"; then
	echo -e "${YELLOW_COLOR}检测到你用的Ubuntu，是否继续换源？${RES}"
	read -r -p "Y(yes);N(no)" input
	case $input in
		[Yy]|"")
			echo "YES"
			echo 'deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic main restricted universe multiverse
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic-updates main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic-updates main restricted universe multiverse
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic-backports main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic-backports main restricted universe multiverse
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic-security main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu-ports/ bionic-security main restricted universe multiverse' >/etc/apt/sources.list && apt update
echo "done" && sleep 1 && MAIN ;;
[Nn])
	echo "NO"
	SOURCES.LIST
        ;;
*)
		INVALID_INPUT
		SOURCES
		;;
esac
elif grep -q 'stretch' "/etc/os-release"; then
        echo -e "${YELLOW_COLOR}\\n
检测到你用的是Debian(stretch)，是否继续换源?${RES}"
	read -r -p"Y(yes); N(no)" input
case $input in
                [Yy]|"")
                echo "YES"
        echo "${SOURCES_ADDR} stretch ${DEB}
${SOURCES_ADDR} stretch-updates ${DEB}
${SOURCES_ADDR} stretch-backports ${DEB}
${SOURCES_SEC} streng/updates ${DEB}" > /etc/apt/sources.list && apt update
echo "done" && sleep 1 && MAIN ;;
[Nn])
        echo "NO"
	SOURCES.LIST
	;;
*)
		INVALID_INPUT
		SOURCES
		;;
esac
elif grep -q 'jessie' "/etc/os-release"; then
        echo -e "${YELLOW_COLOR}\\n
检测到你用的是Debian(jessie)，是否继续换源？${RES}"
	read -r -p"Y(yes); N(no)" input
        case $input in
                [Yy]|"")
                        echo "YES"
                        echo "${SOURCES_ADDR} jessie ${DEB}
${SOURCES_ADDR} jessie-updates ${DEB}
${SOURCES_ADDR} jessie-backports ${DEB}
${SOURCES_SEC} jessie/updates ${DEB}" > /etc/apt/sources.list && apt update
echo "done" && sleep 1 && MAIN ;;
[Nn])
        echo "NO"
        SOURCES.LIST
        ;;
*)
		INVALID_INPUT
		SOURCES
		;;
esac
elif grep -q 'buster' "/etc/os-release"; then
	echo -e "${YELLOW_COLOR}\\n
检测到你用的是Debian(buster)，是否继续换源？${RES}"
	read -r -p"Y(yes); N(no)" input
        case $input in
        [Yy]|"")
        echo "YES"
        echo "${SOURCES_ADDR} buster ${DEB}
${SOURCES_ADDR} buster-updates ${DEB}
${SOURCES_SEC} buster/updates ${DEB}" > /etc/apt/sources.list && apt update
echo "done" && sleep 1 && MAIN ;;
[Nn])
echo "NO"
SOURCES.LIST
;;
*)
		INVALID_INPUT
		SOURCES
		;;
esac

elif grep -q 'sid' "/etc/os-release"; then
	echo -e "${YELLOW_COLOR}\\n
检测到你用的是Debian(sid)，是否继续换源？${RES}"
	read -r -p"Y(yes); N(no)" input
        case $input in
                [Yy]|"")
                        echo "YES"
                        echo "${SOURCES_ADDR} sid ${DEB}" > /etc/apt/sources.list && apt update
			echo "done" && sleep 1 && MAIN ;;
                [Nn])
        echo "no"
        SOURCES.LIST
        ;;
*)
		INVALID_INPUT
		SOURCES
	       	;;
esac
fi
}
#####################
SOURCES.LIST() {
echo -e \\n
if [ -e /etc/os-release ]; then
	printf "你的系统是"
	cat /etc/os-release | head -n 1 | cut -d '"' -f 2
fi
echo -e "${YELLOW_COLOR} 仅支持debian和ubuntu
1) 修改debian或ubuntu国内源
2) 检查源
3) 修复源(如果源出问题)${RES}"
read -r -p "1) 2) 3); E(Exit); M(Main)" input

case $input in
       	1)
		echo "修改debian或ubuntu国内源"
		SOURCES
		;;
	
	2)
		echo "检查源"
		apt update && SOURCES.LIST
		;;

        3)
                echo "fixing..."
		sleep 1
		sed -i "s/https/http/g" /etc/apt/sources.list && apt update && apt install apt-transport-https ca-certificates -y && sed -i "s/http/https/g" /etc/apt/sources.list && apt update && SOURCES.LIST
                ;;
	[eE])                    
		echo "exit"                       
		exit
		;;
	[Mm])
		echo "back to main"
		MAIN
		;;
        *)
		INVALID_INPUT
		SOURCES.LIST
                ;;
esac
}
#######################
MODIFY_DNS()  {
echo -e "${YELLOW_COLOR}是否修改DNS${RES}"
read -r -p "Y(Yes)/N(No);E(Exit);M[Main]" input

case $input in
	[yY]|"")
		echo "Yes"
if [ ! -L "/etc/resolv.conf" ]; then
	echo "nameserver 223.5.5.5
nameserver 223.6.6.6" > /etc/resolv.conf
echo -e "${GREEN_COLOR}已修改为223.5.5.5;223.6.6.6${RES}"          
sleep 1
SETTLE
elif [ -L "/etc/resolv.conf" ]; then
	mkdir -p /run/systemd/resolve 2>/dev/null && echo "nameserver 223.5.5.5
nameserver 223.6.6.6" >/run/systemd/resolve/stub-resolv.conf
echo -e "${GREEN_COLOR}已修改为223.5.5.5;223.6.6.6${RES}"
sleep 1
SETTLE
else
	echo "你的系统不支持"
	sleep 2
	SETTLE
fi
	;;

	[nN])
		echo "No"
		SETTLE
		;;

	[eE]) 
		echo "exit"
		exit 1
		;;
	[Mm])
		echo "back to main"
		MAIN
			;;
	*)
		INVALID_INPUT
		MODIFY_DNS
		;;
esac
}
########################
ADD_GITHUB() {
	echo -e "\n${YELLOW_COLOR}注意，目前仅支持debian(buster)与ubuntu(bionic),建议先安装常用应用${RES}"
	sleep 1
	CHECK
dpkg -l | grep gnupg -q
if [ "$?" != "0" ]; then
apt update && apt install gnupg2
fi 
	echo -e "${YELLOW_COLOR}是否添加Github仓库${RES}"
read -r -p "Y(Yes)/N(No);E(Exit);M[Main]" input

case $input in
	[yY]|"")
		echo "Yes"
		echo "deb https://bintray.proxy.ustclug.org/debianopt/debianopt buster main" > /etc/apt/sources.list.d/debianopt.list && curl -L https://bintray.com/user/downloadSubjectPublicKey?username=bintray | apt-key add - && apt update && SETTLE
		sleep 1
		;;
	[nN])
		echo "No"
		SETTLE
		;;

	[eE])        
		echo "exit"
		exit 1
		;;
	[Mm])
		echo "back to main"
		MAIN
		;;
	*)
		INVALID_INPUT
		ADD_GITHUB
		
		;;
esac
}
###################
VNCSERVER(){
	echo -e "\\n"
	echo -e "${YELLOW_COLOR}1) 安装tightvncserver
2) 安装tigervncserver
3) 安装vnc4server
4) 配置vnc的xstartup参数
5) 设置tigervnc分辨率
6) 创建另一个VNC启动脚本（命令easyvnc）${RES}"
read -r -p "请选择1) 2) 3) 4) 5) 6);E(Exit);M(Main)" input
case $input in
	1)
		echo -e "${YELLOW_COLOR}安装tightvncserver"
	apt install tightvncserver -y
	sed -i "1i\\rm -rf \/tmp\/.X\*" /etc/profile
	echo -e "${BLUE_COLOR}已安装，请输vncserver :1并打开vnc viewer地址输127.0.0.1:1${RES}"
	sleep 2
	INSTALL_SOFTWARE
	;;
2)
	echo "安装tigervncserver"
	apt install tigervnc-standalone-server tigervnc-viewer -y
	sed -i "1i\\rm -rf \/tmp\/.X\*" /etc/profile
	echo -e "${BLUE_COLOR}已安装，请输vncserver :1并打开vnc viewer地址输127.0.0.1:1${RES}"
	sleep 2
	INSTALL_SOFTWARE
	;;
3)
	echo "安装vnc4server"
	apt install vnc4server -y
	sed -i "1i\\rm -rf \/tmp\/.X\*" /etc/profile
	echo -e "${BLUE_COLOR}已安装，请输vncserver :1并打开vnc viewer地址输127.0.0.1:1${RES}"
	sleep 2
	INSTALL_SOFTWARE
	;;
4)
	if [ ! -e ${HOME}/.vnc ]; then
		mkdir ${HOME}/.vnc 
	fi
	echo "#!/bin/bash
unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
#xrdb ${HOME}/.Xresources
export PULSE_SERVER=127.0.0.1
export XKL_XMODMAP_DISABLE=1
#vncconfig -iconic &
startxfce4 &" > ${HOME}/.vnc/xstartup
echo '#!/usr/bin/env bash
unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
if [ $(command -v x-terminal-emulator) ]; then
x-terminal-emulator &
fi
if [ $(command -v xfce4-session) ]; then
 xfce4-session
else
 startxfce4
fi' >/etc/X11/xinit/Xsession
echo -e "${GREEN_COLOR}请选择使用哪个图形界面,xfce4 or lxde${RES}"
        read -r -p "1)xfce4 2)lxde" input
case $input in
	1) echo -e "done" && sleep 2 && VNCSERVER ;;
	2) sed -i "s/startxfce4/startlxde/g" ${HOME}/.vnc/xstartup
		rm /etc/X11/xinit/Xsession
		apt purge --allow-change-held-packages gvfs -y && apt purge --allow-change-held-packages udisk2 -y
		echo -e "done"
		sleep 2
		VNCSERVER
		;;
	*)
		INVALID_INPUT
		VNCSERVER
		;;
esac
;;
5)
	echo -n "输入你手机分辨率,例如 2340x1080  resolution:"
	read resolution
	#sed -i "/^alias/d" /etc/profile && echo "alias vncserver='vncserver -geometry $resolution'" >>/etc/profile
	ex_resolution=`cat /etc/vnc.conf | grep '^$geometry' | cut -d '=' -f 2`
	sed -i "s/$ex_resolution/\"${resolution}\"\;/g" /etc/vnc.conf
	echo "已修改，请重新打开vnc"
	sleep 1
	INSTALL_SOFTWARE
	;;
6)
	echo "创建另一个VNC启动脚本（命令easyvnc）"
	if [ ! -e /usr/bin/tigervncserver ]; then
		echo "检测到你没安装tigervnc，将先安装tigervnc"
		CONFIRM
		apt install tigervnc-standalone-server tigervnc-viewer -y
	fi
	if [ ! -e "${HOME}/.vnc/passwd" ]; then
		echo "请设置vnc密码,6到8位"
		vncpasswd
	fi
	echo -n "输入你手机分辨率,例如 2340x1080  resolution:"
        read resolution
	echo '#!/usr/bin/env bash
pkill -9 Xtightvnc
pkill -9 Xtigertvnc
pkill -9 Xvnc
pkill -9 vncsession
export USER="$(whoami)"
export PULSE_SERVER=127.0.0.1
set -- "${@}" "-ZlibLevel=1"
set -- "${@}" "-securitytypes" "vncauth,tlsvnc"
set -- "${@}" "-verbose"
set -- "${@}" "-ImprovedHextile"
set -- "${@}" "-CompareFB" "1"
set -- "${@}" "-br"
set -- "${@}" "-retro"
set -- "${@}" "-a" "5"
set -- "${@}" "-wm"
set -- "${@}" "-alwaysshared"
set -- "${@}" "-geometry" '$resolution'
set -- "${@}" "-once"
set -- "${@}" "-depth" "16"
set -- "${@}" "-deferglyphs" "16"
set -- "${@}" "-rfbauth" "${HOME}/.vnc/passwd"
set -- "${@}" ":0"
set -- "Xvnc" "${@}"
"${@}" &
export DISPLAY=:0
. /etc/X11/xinit/Xsession 2>/dev/null &
exit 0' >/usr/local/bin/easyvnc && chmod +x /usr/local/bin/easyvnc
echo '#!/usr/bin/env bash
unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
if [ $(command -v x-terminal-emulator) ]; then
x-terminal-emulator &
fi
if [ $(command -v xfce4-session) ]; then
 xfce4-session
else
 startxfce4
fi' >/etc/X11/xinit/Xsession && chmod +x /etc/X11/xinit/Xsession
#echo '#!/usr/bin/env bashunset SESSION_MANAGER
#unset DBUS_SESSION_BUS_ADDRESS
#if [ $(command -v x-terminal-emulator) ]; then
#x-terminal-emulator &
#fi
#if [ $(command -v xfce4-session) ]; then
#xfce4-session
#else
#startxfce4
#fi' >${HOME}/.vnc/xstartup
apt purge --allow-change-held-packages gvfs -y && apt purge --allow-change-held-packages udisk2 -y
echo -e "${GREEN_COLOR}请选择你已安装的图形界面,xfce4 or lxde${RES}"
	read -r -p "1)xfce4 2)lxde" input
	case $input in
	1) echo -e "done" && sleep 2 && VNCSERVER ;;
	2) #sed -i "s/startxfce4/startlxde/g" ${HOME}/.vnc/xstartup
		#sed -i "s/xfce4-session/lxsession/g" ${HOME}/.vnc/xstartup
		sed -i "s/startxfce4/startlxde/g" /etc/X11/xinit/Xsession
		sed -i "s/xfce4-session/lxsession/g" /etc/X11/xinit/Xsession
echo -e "Done\n打开vnc viewer地址输127.0.0.1:0\nvnc的退出，在系统输exit即可"
sleep 2
VNCSERVER
;;
esac ;;
[Ee])
	echo "exit"
	exit 1
	;;
[Mm])
	MAIN
	;;
*)
		INVALID_INPUT
		INSTALL_SOFTWARE
		;;
esac
}

##########################
WEB_BROWSER() {
	echo -e "${YELLOW_COLOR}1) 安装谷歌浏览器chromium
2) 安装火狐浏览器firefox
3) 安装epiphany-browser浏览器${RES}"
	read -r -p "1) 2) 3);E(Exit);M(Main)" input
	case $input in
	1)
			echo -e "安装谷歌浏览器chromium"
			if grep -q 'ID=debian' "/etc/os-release"; then
        apt install chromium -y
elif grep -q 'ID=ubuntu' "/etc/os-release"; then
        apt install chromium-browser -y
else
        echo -e "${RED_COLOR}你用的不是Debian或Ubuntu系统，操作将中止...${RES}"
sleep 2
WEB_BROWSER
fi
	if [ -e "/usr/share/applications/chromium.desktop" ]; then
		sed -i "s/Exec=\/usr\/bin\/chromium %U/Exec=\/usr\/bin\/chromium --no-sandbox \%U/g" /usr/share/applications/chromium.desktop
	elif [ -e "/usr/share/applications/chromium-browser.desktop"]; then
		sed -i "s/Exec=chromium-browser %U/Exec=chromium-browser --no-sandbox \%U/g" /usr/share/applications/chromium-browser.desktop
		fi
		echo -e "${YELLOW_COLOR}已完成安装并配置完成${RES}"
	sleep 1
	WEB_BROWSER
	;;
2)
	echo -e "${YELLOW_COLOR}安装火狐浏览器firefox${RES}"
	if grep -q 'ID=debian' "/etc/os-release"; then                 apt install firefox-esr -y && sed -i "s/firefox-esr %u/firefox-esr --no-sandbox \%u/g" /usr/share/applications/firefox-esr.desktop 2>/dev/null
	elif grep -q 'ID=ubuntu' "/etc/os-release"; then
		apt install firefox firefox-locale-zh-hans ffmpeg
	else
		echo -e "${RED_COLOR}你用的不是Debian或Ubuntu系统，操作将中止...${RES}"
		sleep 2
		WEB_BROWSER
	fi
	echo -e "${YELLOW_COLOR}已完成安装并配置完成${RES}"
	sleep 1
	WEB_BROWSER
	;;
3)
	echo -e "${YELLOW_COLOR}安装浏览器epiphany-browser${RES}"
	apt install epiphany-browser
	echo -e "${YELLOW_COLOR}done${RES}"
	sleep 1
	WEB_BROWSER
	;;
	[Ee])
		echo "exit"
		exit 1
		;;
	[Mm])
		MAIN
		;;
	*)
		INVALID_INPUT
		WEB_BROWSER
		;;
esac
}


INSTALL_SOFTWARE() {
	echo -e "\\n"
	echo -e "${RED_COLOR}注意，建议先安装常用应用${RES}"
	echo -e "${BLUE_COLOR}1) 安装常用应用(目前包括curl,wget,vim,fonts-wqy-zenhei,tar)
2) 安装Electron(将先安装GitHub仓库)
3) 安装xfce4,简装
4) 安装vncserver
5) 安装浏览器
6) 安装非官方版electron-netease-cloud-music(需先安装GitHub仓库与Electron)
7) 安装谷歌输入法
8) 安装mpv播放器
9) 安装libreoffice
10) 安装lxde,简装
11) 安装dosbox 并配置dosbox文件目录（需先运行一次dosbox以生成配置文件）${RES}"
read -r -p "1) 2) 3) 4) 5) 6) 7) 8) 9) 10) 11);E(Exit);M(Main)" input

case $input in
	1)
		echo "安装常用应用..."
		apt install curl wget vim fonts-wqy-zenhei tar -y
		echo -e "${YELLOW_COLOR}done${RES}"
		sleep 1
		INSTALL_SOFTWARE
		
		;;

	2)
		echo "安装Electron"
		echo "deb https://bintray.proxy.ustclug.org/debianopt/debianopt buster main" > /etc/apt/sources.list.d/debianopt.list && curl -L https://bintray.com/user/downloadSubjectPublicKey?username=bintray | apt-key add - && apt update && apt install electron -y
		echo -e "${YELLOW_COLOR}done${RES}"
		sleep 1
		INSTALL_SOFTWARE
		;;
	3)
		echo "安装xfce4"
		apt install xfce4 xfce4-terminal ristretto -y
		echo -e "${YELLOW_COLOR}done${RES}"
		sleep 1
		INSTALL_SOFTWARE
		;;
	4)
		echo "入vncserver安装选择"
		VNCSERVER
		;;
	5)
		WEB_BROWSER
		;;
	6)
		dpkg -l | grep electron -q
if [ "$?" == '0' ]; then
echo -e "${BLUE_COLOR}检测到已安装Electron${RES}"
sleep 1
else
echo -e "${BLUE_COLOR}检测到你未安装Electron，将安装GitHub仓库与Electron${RES}"
echo "deb https://bintray.proxy.ustclug.org/debianopt/debianopt buster main" > /etc/apt/sources.list.d/debianopt.list && curl -L https://bintray.com/user/downloadSubjectPublicKey?username=bintray | apt-key add - && apt update && apt install electron -y
fi
echo "正在安装非官方版electron-netease-cloud-music"
apt install electron-netease-cloud-music
if [ -e /usr/bin/electron-netease-cloud-music ]; then
	echo "#!/bin/bash
exec electron /opt/electron-netease-cloud-music/app.asar --no-sandbox" > /usr/bin/electron-netease-cloud-music
echo -e "${BLUE_COLOR}请在图形界面查看是否安装成功${RES}"
else
	echo -e "${RED_COLOR}安装失败${RES}"
fi
sleep 1
INSTALL_SOFTWARE
;;
7)
	echo -e "${YELLOW_COLOR}安装谷歌输入法${RES}"
	apt install fcitx*googlepinyin*
	echo -e "${YELLOW_COLOR}done${RES}"
	sleep 1
	INSTALL_SOFTWARE
	;;
[8])
	echo "安装mpv播放器"
	apt install mpv
	echo -e "${BLUE_COLOR}done${RES}"
		sleep 1
		INSTALL_SOFTWARE
		;;
	[9])
		echo "安装libreoffice"
		apt install libreoffice
		echo -e "${BLUE_COLOR}done${RES}"
		sleep 1
		INSTALL_SOFTWARE
		;;
	[eE])
		echo "exit"
		exit 1
		;;
	[Mm])
		echo "back to main"
		MAIN
		;;
	10)
		echo "安装lxde"
		apt install lxde-core lxterminal dbus-x11 -y
		echo -e "${YELLOW_COLOR}done${RES}"
		sleep 1
		INSTALL_SOFTWARE
		;;
	11) DOSBOX ;;
	*) INVALID_INPUT                                                                                                                             INSTALL_SOFTWARE ;;
esac
}
##################
DOSBOX() {
	echo -e "\n1）安装dosbox
2）创建dos运行文件目录"
		read -r -p "请选择1) 2) E(exit);M(main)" input
		case $input in
			1) echo "安装dosbox"
				apt install dosbox -y
				echo -e "done\n如需创建dos运行文件目录，需先运行一次dosbox以生成配置文件"
				CONFIRM
				INSTALL_SOFTWARE ;;
			2) rm -rf ${HOME}/DOS && mkdir ${HOME}/DOS
		if [ ! -e ${HOME}/.dosbox ]; then
			echo -e "\n${RED_COLOR}未检测到dosbox配置文件，请先运行一遍dosbox，再做此步操作${RES}"
			sleep 2
		else
		dosbox=`ls ${HOME}/.dosbox`
                sed -i "/^\[autoexec/a\mount c ${HOME}/DOS" ${HOME}/.dosbox/$dosbox
		echo -e "${GREEN_COLOR}配置完成，请把运行文件夹放在主目录DOS文件夹里${RES}"
		sleep 2
	fi
		INSTALL_SOFTWARE ;;
	[Ee]) 
		exit 0 ;;
	[Mm]) MAIN ;;
	*) INVALID_INPUT
		INSTALL_SOFTWARE ;;
esac
}
#####################
INSTALL_PYTHON3() {
echo -e "${YELLOW_COLOR}安装python3和pip并配置国内源${RES}"
read -r -p "Y(Yes)/N(No);E(Exit);M[Main]" input
case $input in
	[Yy]|"")
		echo "yes"
		apt install python3 python3-pip && mkdir /root/.config/pip && echo "[global]
index-url = https://pypi.tuna.tsinghua.edu.cn/simple" > /root/.config/pip/pip.conf
echo -e "${BLUE_COLOR}done${RES}"
sleep 1
SETTLE
		;;
	[Nn])
		echo "no"
		SETTLE
		;;
	[Ee])
		echo "exit"
		exit 1
		;;
	[Mm])
		echo "back to Main"
		MAIN
		;;
	*)
		INVALID_INPUT
		INSTALL_PYTHON3
		;;

esac



}
#################
QEMU_SYSTEM() {
echo -e "
1) 安装qemu-system-x86_64(64位系统模拟器)，并联动更新模拟器所需应用
2) 创建windows镜像目录
3) 启动qemu-system-x86_64模拟器
4) 还没准备好
5) 退出"
read -r -p "请选择" input
case $input in
1) pkg update -y 2>/dev/null
	apt update -y && apt install qemu-system-x86* -y
        QEMU_SYSTEM
        ;;
2) echo "创建windows镜像目录及共享目录"
        if [ ! -e "/sdcard/windows" ]; then
                mkdir /sdcard/windows
        fi
	if [ ! -e "/sdcard/share/" ]; then
		mkdir /sdcard/share
	fi
	`ls /sdcard/windows` || `ls /sdcard/share`
	if [ "$?" != "0" ]; then
		echo -e "${RED_COLOR}创建目录失败${RES}"
	else
        echo -e "${GREEN_COLOR}手机根目录下已创建windows文件夹，请把系统镜像放进这个目录里\n共享目录是share(目录内总文件大小不能超过500m)${RES}"
	fi
        CONFIRM
	QEMU_SYSTEM
        ;;
3) echo -e "${GREEN_COLOR}请确认系统镜像已放入手机目录windows里${RES}"
	CONFIRM
	pkill -9 qemu-system-x86
        echo -n "请输入系统镜像全名（例如andows.img）mir_name:"
	read mir_name
	echo -n "请输入模拟的内存大小，以m为单位（1g=1024m，例如512）mem:"
	read mem
	read -r -p "请选择cpu 1)core2duo 2)athlon 3)pentium2 4)n270" input
	case $input in
	1) set -- "${@}" "-cpu" "core2duo"
		set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1" ;;	
	2) set -- "${@}" "-cpu" "athlon"
		set -- "${@}" "-smp" "2,cores=2,threads=1,sockets=1" ;;
	3) set -- "${@}" "-cpu" "pentium2"
		set -- "${@}" "-smp" "1,cores=1,threads=1,sockets=1" ;;
	*) set -- "${@}" "-cpu" "n270"
		set -- "${@}" "-smp" "2,cores=1,threads=2,sockets=1" ;;
esac
	read -r -p "请选择显卡 1)cirrus 2)vmware" input
	case $input in
		1) set -- "${@}" "-vga" "cirrus" ;;
		*) set -- "${@}" "-vga" "vmware" ;;
	esac
	read -r -p "请选择网卡 1)e1000 2)rtl8139" input
	case $input in
		1) set -- "${@}" "-net" "nic,model=e1000" ;;
		*) set -- "${@}" "-net" "nic,model=rtl8139" ;;
	esac
	read -r -p "请选择声卡 1)ac97 2)sb16 3)es1370" input
        case $input in
		1) set -- "${@}" "-soundhw" "ac97" ;;
		2) set -- "${@}" "-soundhw" "sb16" ;;
		*) set -- "${@}" "-soundhw" "es1370" ;;
	esac
	set -- "${@}" "-hda" "/sdcard/windows/$mir_name"
	set -- "${@}" "--accel" "tcg,thread=multi"
	set -- "${@}" "-m" "$mem"
#	set -- "${@}" "-hdb" "/sdcard/"
	set -- "${@}" "-hdd" "fat:rw:/sdcard/share/"
	set -- "${@}" "-net" "user"
	set -- "${@}" "-vnc" ":0"
	set -- "qemu-system-x86_64" "${@}"
	"${@}" & ;;
4) echo "没准备好"
        sleep 1
        QEMU_SYSTEM
        ;;
5) MAIN ;;
*) INVALID_INPUT && QEMU_SYSTEM ;;
esac
}
#################

TERMUX() {
	echo -e "\n\e[1;32m注意！以下均在termux环境中操作${RES}"
	echo -e "${BLUE_COLOR}1) 修改termux键盘
2) termux换清华源
3) 安装常用应用(包括curl tar wget vim proot)
4) 安装pulseaudio并配置
5) 创建root用户系统登录脚本
6) 下载Debian(buster)系统
7) 下载Ubuntu(bionic)系统
8) qemu-system-x86_64模拟器
9) 防手机后台杀termux进程(似乎无效)${RES}"
read -r -p "CHOOSE: 1) 2) 3) 4) 5) 6) 7) 8）9);E(Exit);M[Main]" input
case $input in
	1)
		echo "修改键盘"
		if [ -d ${HOME}/.termux ]; then
			rm -rf ${HOME}/.termux
		fi
		mkdir ${HOME}/.termux && echo "extra-keys = [ \
['ESC','|','/','HOME','UP','END','PGUP','-'], \
['TAB','CTRL','ALT','LEFT','DOWN','RIGHT','PGDN','~'] \
]" >${HOME}/.termux/termux.properties
		echo "已修改，请重启termux"
		sleep 1
		TERMUX
		;;
	2)
		echo -e "正在更换清华源"
		sleep 1
		sed -i 's@^\(deb.*stable main\)$@#\1\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/termux-packages-24 stable main@' $PREFIX/etc/apt/sources.list
sed -i 's@^\(deb.*games stable\)$@#\1\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/game-packages-24 games stable@' $PREFIX/etc/apt/sources.list.d/game.list
sed -i 's@^\(deb.*science stable\)$@#\1\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/science-packages-24 science stable@' $PREFIX/etc/apt/sources.list.d/science.list
apt update && apt upgrade
echo -e  "已更换为清华源"
TERMUX
	;;
3)
	echo "安装常用应用(curl tar wget proot)"
	pkg install curl tar wget vim proot
	echo "已安装"
	TERMUX
	;;
4)
	echo "安装并配置pulseaudio"
	wget https://andronixos.sfo2.cdn.digitaloceanspaces.com/OS-Files/setup-audio.sh && chmod +x setup-audio.sh && ./setup-audio.sh
	if [ ! -e ${HOME}/setup-audio.sh ]; then
		echo -e "${RED_COLOR}\n安装失败，请重试${RES}"
		sleep 2
		TERMUX
	else
		echo -e "${GREEN_COLOR}重新配置待机时长...${RES}"
        sed -i "s/180/-1/g" ${PREFIX}/etc/pulse/daemon.conf
	sleep 2
	fi
		echo -e "\n已安装并配置pulseaudio"
	sleep 1
	TERMUX
	;;
5)
	echo -n "请把系统文件夹放根目录并输入系统文件夹名字rootfs:"
	read rootfs
	if [ -e $rootfs.sh ]; then
		rm -rf $rootfs.sh
	fi
	echo "" >$rootfs/proc/version
		echo "pulseaudio --start &
unset LD_PRELOAD
proot --kill-on-exit -S $rootfs --link2symlink -b /sdcard:/root/sdcard -b $rootfs/proc/version:/proc/version -b $rootfs/root:/dev/shm -w /root /usr/bin/env -i HOME=/root TERM=$TERM USER=root PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games LANG=C.UTF-8 /bin/bash --login" > $rootfs.sh && chmod +x $rootfs.sh
echo "已创建root用户系统登录脚本,登录方式为./$rootfs.sh"
if [ -e ${PREFIX}/etc/bash.bashrc ]; then
	if ! grep -q 'pulseaudio' ${PREFIX}/etc/bash.bashrc; then
		sed -i "1i\pkill -9 pulseaudio" ${PREFIX}/etc/bash.bashrc
	fi
	else
	sed -i "1i\pkill -9 pulseaudio" $rootfs.sh
fi
sleep 2
TERMUX
;;
6) echo -e "由于系统包很干净，所以进入系统后，建议再用本脚本到'系统相关'修改dns和源地址及安装常用应用"
	CONFIRM
	echo "下载Debian(buster)系统..."
	sleep 1
	if [ -e rootfs.tar.xz ]; then
		rm -rf rootfs.tar.xz
	fi
		curl -o rootfs.tar.xz https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/debian/buster/arm64/default/
		VERSION=`cat rootfs.tar.xz | grep href | tail -n 2 | cut -d '"' -f 4 | head -n 1`
		curl -o rootfs.tar.xz https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/debian/buster/arm64/default/${VERSION}rootfs.tar.xz
		echo -e "${YELLOW_COLOR}因清华大学源经常卡顿，如系统无法启动，请重新下载${RES}"
		sleep 2
		echo -n "请给系统文件夹起个名bagname:"
		read bagname
		if [ -e $bagname ]; then
			rm -rf $bagname
			fi
			mkdir $bagname && tar xvf rootfs.tar.xz -C $bagname
			#tar xf rootfs.tar.xz --checkpoint=100 --checkpoint-action=dot --totals
		rm rootfs.tar.xz
		echo -e "${BLUE_COLOR}Debian(buster)系统已下载，文件夹名为$bagname${RES}"
		sleep 2
		TERMUX
		;;
	7) echo -e "由于系统包很干净，所以建议进入系统后，再用本脚本到'系统相关'修改dns和源地址及安装常用应用"
		CONFIRM
		echo "下载Ubuntu(bionic)系统..."
	sleep 1
	if [ -e rootfs.tar.xz ]; then
	rm -rf rootfs.tar.xz
	fi
	curl -o rootfs.tar.xz https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/ubuntu/bionic/arm64/default/
	VERSION=`cat rootfs.tar.xz | grep href | tail -n 2 | cut -d '"' -f 4 | head -n 1`
                curl -o rootfs.tar.xz https://mirrors.tuna.tsinghua.edu.cn/lxc-images/images/ubuntu/bionic/arm64/default/${VERSION}rootfs.tar.xz
                echo -e "${YELLOW_COLOR}因清华大学源经常卡顿，如系统无法启动，请重新下载${RES}"
		sleep 2
		echo -n "请给系统文件夹起个名bagname:"
		read bagname
		if [ -e $bagname ]; then
		rm -rf $bagname
		fi
		mkdir $bagname && tar xvf rootfs.tar.xz -C $bagname
		rm rootfs.tar.xz
		echo -e "${BLUE_COLOR}Ubuntu(bionic)系统已下载，文件夹名为$bagname${RES}"
		sleep 2
		TERMUX
		;;
	8) QEMU_SYSTEM ;;
	9) echo -e "请选择是否打开termux防后台进程被杀（这是termux自带命令，不确定是否有效）"
		read -r -p "1)打开 2)关闭" input
		case $input in
			1) echo "yes"
				termux-wake-lock
				TERMUX ;;
			2) echo "no"
				termux-wake-unlock
				TERMUX ;;
			*) INVALID_INPUT
				TERMUX ;;
		esac ;;
	[nN])
		echo "no"
		MAIN
		;;

	[Ee])
		echo "exit"
		exit 1
		;;
	[Mm])
		echo "back to Main"
		MAIN
		;;
	*)
		INVALID_INPUT
		TERMUX
		;;
esac
}
#########################
MAIN() {	

	echo -e "${YELLOW_COLOR}1) 软件安装
2) 系统相关
3) termux相关(包括系统包下载)
4) exit${RES}"
read -r -p "CHOOSE: 1) 2) 3) 4)" input
case $input in
        1) INSTALL_SOFTWARE ;;
	2) SETTLE ;;
	3) TERMUX ;;
	4) exit 1 ;;
	*) INVALID_INPUT
		MAIN ;;
esac
}
###############
MAIN "$@"
