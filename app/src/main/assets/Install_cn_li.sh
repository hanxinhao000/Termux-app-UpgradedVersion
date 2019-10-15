#!/bin/sh

# Set Image address
# 设置镜像地址(使用Gitee源以加速大陆地区速度)
#export ADDRESS=https://gitee.com/ZhangHuaGitee/fedora-termux-cn/raw/master/fedora.7z
export ADDRESS=https://gitlab.com/zhanghua000/fedora-termux-cn/raw/master/fedora.7z
# This is GitHub mirror. Don't use if you live in China mainland.
# 上一行为GitHub上的镜像，大陆地区不建议使用

# install necessary packages
# 安装必备软件包
echo "正在安装依赖软件包: proot tar wget git p7zip"
pkg install proot tar wget git p7zip -y

# create local directory and get files
# 创建本地目录并且获取文件
echo "正在创建目录..."
#mkdir ~/fedora_cn
cd ~/xinhao_fedora
echo "正在获取镜像并解压缩..."
#wget $ADDRESS -O fedora.7z
7z x fedora.7z
echo "正在清理临时文件..."
rm -f fedora.7z

# generate start script
# 生成启动脚本
echo "正在生成启动脚本..."
cat > /data/data/com.termux/files/usr/bin/startfedora <<- EOM
#!/bin/sh
unset LD_PRELOAD && proot --link2symlink -0 -r ~/fedora -b /dev/ -b /sys/ -b /proc/ -b /storage/ -b $HOME -w $HOME /bin/env -i HOME=/root TERM="$TERM" PS1='[termux@fedora \W]\$ ' LANG=en_US.UTF8 PATH=/bin:/usr/bin:/sbin:/usr/sbin /bin/zsh -l
EOM
chmod +x /data/data/com.termux/files/usr/bin/startfedora

# finish infomation:
# 结束信息:
echo "安装完毕! 用 'startfedora' 启动Fedora. 在Fedora终端里面执行 'dnf update' 来更新Fedora. "
