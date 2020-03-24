sed -i 's@^\(deb.*stable main\)$@#\1\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux stable main@' $PREFIX/etc/apt/sources.list
apt update
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install tar proot openssl-tool -y
apt install wget -y
cd ~
wget http://hssfjd.iok.la:81/Linux/Ubuntu/ubuntu-ports.tar.gz
tar -xvf ubuntu-ports.tar.gz
mv data/data/com.termux/files/home/ubuntu-fs ~/
mv data/data/com.termux/files/home/ubuntu-binds ~/
mv data/data/com.termux/files/home/start-ubuntu.sh ~/
rm -rf ~/data
rm -rf ~/ubuntu-ports.tar.gz
clear
echo "deb https://dl.bintray.com/termux/termux-packages-24 stable main" > ~/../usr/etc/apt/sources.list
echo -e "Ubuntu安装完毕\n启动命令:\n###→ \033[32m~/start-ubuntu.sh\033[0m ←###\nVNC启动命令:\033[32mstartvnc\033[0m\nVNC关闭命令:\033[32mstopvnc\033[0m"
echo "输入lisky-upgrade更新脚本"
echo "wget hssfjd.iok.la:81/Linux/Ubuntu/bak.sh && bash bak.sh" > ~/../usr/bin/lisky-upgrade
chmod +x ~/../usr/bin/lisky-upgrade
rm -rf ~/ub*.sh
