#!/data/data/com.termux/files/usr/bin/bash
folder=raspbian-fs
if [ -d "$folder" ]; then
	first=1
	echo "skipping downloading"
fi
tarball="raspios_armhf_rootfs.tar.xz"
if [ "$first" != 1 ];then
	if [ ! -f $tarball ]; then
		echo -e "\e[1;33m 检查你的设备 \e[0m"
		case `dpkg --print-architecture` in
		arm)
			archurl="armhf" ;;
		*)
			echo -e "\e[1;31m 当前只支持armhf架构的原版Tremux_Os系统 \e[0m"; exit 1 ;;
		esac
		wget "http://127.0.0.1:19956/user/raspios_armhf_rootfs.tar.xz"
	fi
	cur=`pwd`
	mkdir -p "$folder"
	cd "$folder"
	echo "解压Rootfs，请耐心等待。"
	proot --link2symlink tar -xJf ${cur}/${tarball}||:
	cd "$cur"
fi
bin=start-raspbian.sh
echo "writing launch script"
cat > $bin <<- EOM
#!/bin/bash
cd \$(dirname \$0)
## unset LD_PRELOAD in case termux-exec is installed
unset LD_PRELOAD
command="proot"
command+=" --link2symlink"
command+=" -0"
command+=" -r $folder"
command+=" -b /dev"
command+=" -b /proc"
command+=" -b raspbian-fs/root:/dev/shm"
## uncomment the following line to have access to the home directory of termux
#command+=" -b /data/data/com.termux/files/home:/root"
## uncomment the following line to mount /sdcard directly to / 
#command+=" -b /sdcard"
command+=" -w /root"
command+=" /usr/bin/env -i"
command+=" HOME=/root"
command+=" PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games"
command+=" TERM=\$TERM"
command+=" LANG=C.UTF-8"
command+=" /bin/bash --login"
com="\$@"
if [ -z "\$1" ];then
    exec \$command
else
    \$command -c "\$com"
fi
EOM

echo "fixing shebang of $bin"
termux-fix-shebang $bin
chmod +x $bin
rm $tarball
echo "You can now launch Debian with the ./${bin} script"
echo "Script BY EXALAB Github And AkiraYuki"
