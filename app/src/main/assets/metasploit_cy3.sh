#!/data/data/com.termux/files/usr/bin/bash
echo "
















"
clear
echo "      _____     __  __ __________ _  _"
echo "     |_   _|   |  \/  |___ /_   _| || |"
echo "       | |_____| |\/| | |_ \ | | | || |_" 
echo "       | |_____| |  | |___) || | |__   _|"
echo "       |_|     |_|  |_|____/ |_|    |_| "
echo "
"
echo "       -{ Termux - Metaslpoit  } "
echo "       -{ Coded by V3rluchie   } "
echo ""
echo ""
echo "Using And Installing This Tools That Mean You Ready For"
echo ""
echo "[1] Use At Your Own Risk"
echo "[2] No Warranty"
echo "[3] If it violates the law with this tool the risk is borne by the user"
echo ""
echo "-------------------------------------------"
echo "Are You Sure Want To Install Metasploit ?"
echo "-------------------------------------------"
echo ""
echo "Press Enter if you Agree / Continue Install"
echo "Press CTRL + C if you Disagree / Cancel Install"
read ENTER

echo "####################################"
apt install autoconf bison clang coreutils curl findutils git apr apr-util libffi-dev libgmp-dev libpcap-dev postgresql-dev readline-dev libsqlite-dev openssl-dev libtool libxml2-dev libxslt-dev ncurses-dev pkg-config postgresql-contrib wget make ruby-dev libgrpc-dev ncurses-utils termux-tools -y
echo "####################################"
echo "Downloading & Extracting....."

cd $HOME
curl -LO https://github.com/rapid7/metasploit-framework/archive/4.16.4.tar.gz
tar -xf $HOME/4.16.4.tar.gz
mv $HOME/metasploit-framework-4.16.4 $HOME/metasploit-framework
cd $HOME/metasploit-framework
sed '/rbnacl/d' -i Gemfile.lock
sed '/rbnacl/d' -i metasploit-framework.gemspec
gem install bundler
bundle config build.nokogiri --use-system-libraries


gem install nokogiri -- --use-system-libraries
 
sed 's|grpc (.*|grpc (1.4.1)|g' -i $HOME/metasploit-framework/Gemfile.lock
gem unpack grpc -v 1.4.1
cd grpc-1.4.1
curl -LO https://raw.githubusercontent.com/grpc/grpc/v1.4.1/grpc.gemspec
curl -L https://wiki.termux.com/images/b/bf/Grpc_extconf.patch -o extconf.patch
patch -p1 < extconf.patch
gem build grpc.gemspec
gem install grpc-1.4.1.gem
cd ..
rm -r grpc-1.4.1


cd $HOME/metasploit-framework
bundle install -j5

$PREFIX/bin/find -type f -executable -exec termux-fix-shebang \{\} \;
rm ./modules/auxiliary/gather/http_pdf_authors.rb
ln -s $HOME/metasploit-framework/msfconsole /data/data/com.termux/files/usr/bin/


echo "Thanx  To  Yukinoshita"
echo ""
echo "For your support"
echo "And For Team "
echo " Blood Tears No Team Squad "
echo "

"
echo " contact me : verluchie[at]hackermail.com "
echo "

"
echo " NOW YOU CAN LAUNCH METASPLOIT BY JUST EXECUTE THE COMMAND :=> msfconsole"
