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
echo -e "Ubuntu安装完毕\n启动命令:\n###→ \033[32m~/start-ubuntu.sh\033[0m ←###\nVNC启动命令:\033[32mstartvnc\033[0m\nVNC关闭命令:\033[32mstopvnc\033[0m"
rm -rf ~/ubuntu.sh
