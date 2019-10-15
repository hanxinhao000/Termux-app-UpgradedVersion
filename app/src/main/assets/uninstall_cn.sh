#!/bin/sh
chmod +777 -R ~/fedora_cn
rm -rf ~/.fedora_cn
chmod +777 $PREFIX/bin/startfedora
rm -f $PREFIX/bin/startfedora
echo "卸载完成."