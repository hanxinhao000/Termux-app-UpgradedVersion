#!/data/data/com.termux/files/usr/bin/bash
folder=ubuntu-fs
if [ -d "$folder" ]; then
    first=1
    printf 'Skipping the download\n'
fi
if [ "$first" != 1 ];then
    if [ ! -f "fedora.tar.xz" ]; then
        printf 'Downloading the fedora rootfs...\n'
        if [ "$(dpkg --print-architecture)" = "aarch64" ];then
            wget http://127.0.0.1:19956/user/fedora_arm64_rootfs.tar.xz -O fedora.tar.xz -q
        elif [ "$(dpkg --print-architecture)" = "arm" ];then
            echo "不支持的架构"
            exit 1
        elif [ "$(dpkg --print-architecture)" = "x86_64" ];then
            wget http://127.0.0.1:19956/user/fedora_amd_rootfs.tar.xz -O fedora.tar.xz -q
        elif [ "$(dpkg --print-architecture)" = "x86" ];then
             echo "不支持的架构"
             exit 1
        elif [ "$(dpkg --print-architecture)" = "amd64" ];then
            wget http://127.0.0.1:19956/user/fedora_amd_rootfs.tar.xz -O fedora.tar.xz -q
        elif [ "$(dpkg --print-architecture)" = "i686" ];then
             echo "不支持的架构"
             exit 1
        elif [ "$(dpkg --print-architecture)" = "i386" ];then
             echo "不支持的架构"
             exit 1
        elif [ "$(dpkg --print-architecture)" = "i586" ];then
             echo "不支持的架构"
             exit 1




        else
            printf 'unknown architecture\n'
            exit 1
        fi

        echo "下载完成"

    fi

fi

echo "开始安装";
cur=`pwd`
linux="fedora"
mkdir -p "$linux"
cd "$linux"
echo "正在解压rootfs，请稍候"
proot --link2symlink tar -xJf ${cur}/${linux}.tar.xz --exclude='dev' --exclude='etc/rc.d' --exclude='usr/lib64/pm-utils'
echo "更新DNS"
echo "127.0.0.1 localhost" > etc/hosts
rm -rf etc/resolv.conf &&
echo "nameserver 114.114.114.114" > etc/resolv.conf
echo "nameserver 8.8.4.4" >> etc/resolv.conf
echo "export  TZ='Asia/Shanghai'" >> root/.bashrc
cd "$cur"
if [ $linux == "fedora" ]; then
	bash_tmp="bash";
else
	bash_tmp="sh";
fi

if [ $linux == "ubuntu" ]; then
	touch "${linux}/root/.hushlogin"
fi
bin=start-${linux}.sh
echo "写入启动脚本"
cat > $bin <<- EOM
#!/bin/bash
cd \$(dirname \$0)
## unset LD_PRELOAD in case termux-exec is installed
pulseaudio --start &
echo "" &
echo "欢迎来到fedora系统" &
echo "------------------" &
echo "|                |" &
echo "|     fedora     |" &
echo "|                |" &
echo "------------------" &
echo "" &
echo "" &
echo "" &
unset LD_PRELOAD
command="proot"
command+=" --link2symlink"
command+=" -0"
command+=" -r $linux"
command+=" -b /dev"
command+=" -b /proc"
command+=" -b $linux/root:/dev/shm"
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
command+=" /bin/${bash_tmp} --login"
com="\$@"
if [ -z "\$1" ];then
    exec \$command
else
    \$command -c "\$com"
fi
EOM

echo "fixing shebang of $bin"
termux-fix-shebang $bin
echo "授予 $bin 执行权限"
chmod +x $bin
echo "正在删除镜像文件"
rm $linux.tar.xz
echo "现在可以执行 ./${bin} 运行 ${linux} 了"
