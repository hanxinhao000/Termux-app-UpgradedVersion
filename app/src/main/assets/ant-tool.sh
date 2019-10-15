# 开发by urain39
# 技术交流请加群494453985

TARGET=$1
OPTION=$2

usage() {
cat <<EOF
$0 [system] [mount|umount]
$0 [system] [switch]
EOF
}

case $OPTION in
mount)
    # /proc
    mount /proc $TARGET/proc
    # /sys
    mount /sys $TARGET/sys
    # /dev
    mount /dev $TARGET/dev
    ;;
umount)
    # /proc
    umount $TARGET/proc
    # /sys
    umount $TARGET/sys
    # /dev
    umount $TARGET/dev
    ;;
switch)
        echo "Server = http://mirrors.tuna.tsinghua.edu.cn/archlinuxarm/\$arch/\$repo" > $TARGET/etc/pacman.d/mirrorlist
    ;;
*)
    usage
    ;;
esac
