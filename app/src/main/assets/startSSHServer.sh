#! /bin/bash

if [ ! -f /support/.ssh_setup_complete ]; then
    rm -rf /etc/dropbear
    mkdir /etc/dropbear
    dropbearkey -t dss -s 1024 -f /etc/dropbear/dropbear_dss_host_key
    dropbearkey -t rsa -s 2048 -f /etc/dropbear/dropbear_rsa_host_key
    dropbearkey -t ecdsa -s 521 -f /etc/dropbear/dropbear_ecdsa_host_key
    touch /support/.ssh_setup_complete
fi

dropbear -E -p 2022
