# 这是一种chroot运行Linux的解决方案
# 如果基于本脚本改的Linux运行方案麻烦
# 标注一下原作者什么的，谢谢啦！

# TGZ文件路径
TGZ_PATH=$PWD/ArchLinuxARM-armv7-latest.tar.gz
# 运行路径
RUN_PATH=$PWD/archlinux

# 解包tar文件
if [ -e $TGZ_PATH ]; then
    if [ -e $RUN_PATH ]; then
        echo "已存在运行目录"
    else
        echo "未存在运行目录"
        echo "正在创建运行目录"
        mkdir -p $RUN_PATH

        echo "正在进入运行目录"
        cd $RUN_PATH

        echo "开始解包$TGZ_PATH"
        sleep 2 # 这里主要是给开发者留下停止这个脚本的时间
        tar -x -v -f $TGZ_PATH
    fi
else
    echo "没有找到tgz文件！"
    exit 1
fi

###############################################
if [ ! -e $RUN_PATH/init.sh ]; then
# chroot状态下的初始化脚本
cat > $RUN_PATH/init.sh <<EOF
#!/usr/bin/bash
# 初始化全局变量
export HOSTNAME="termux"
export TERM="xterm"
export HOME="/root"
export TMPDIR="/tmp"
export PS1='[\u@termux \W]'

# 导入全局变量
. /etc/profile

# 进入HOME目录
cd \$HOME

cat <<TUIGUANG
  _____                      
 |_   _|__ _ _ _ __ _  ___ __
   | |/ -_) '_| '  \ || \ \ /
   |_|\___|_| |_|_|_\_,_/_\_\
                             
欢迎进入termux-archlinux
更多termux交流请加群494453985

注意事项：
    1.本程序为在chroot或是proot情况下执行，所以
    错误较多，大多数直接忽略就好。(chroot兼容性
    很好，建议chroot模式运行)
    2.root用户请谨慎使用，造成设备损坏本人一律
    不负责。
    3.安装软件时如果不能安装可以试试加上--forc
    e参数！！！（危险，但对装在手机上的系统不算
    什么了）
TUIGUANG
# 以下代码我觉得拖慢运行速度，造成程序运行不稳定
# 所以我觉得删了更好，元芳你怎么看？
sh
EOF
fi
##################################################

chmod 755 $RUN_PATH/init.sh

if [ $(whoami) != "root" ]; then
     # 个人很不建议proot运行！
     proot $RUN_PATH /init.sh
else
    chroot $RUN_PATH /init.sh
fi

