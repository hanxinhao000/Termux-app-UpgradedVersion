#! /bin/bash

if [[ -z "${DISPLAY}" ]]; then
  DISPLAY=:4713
fi

if [[ -z "${PULSE_SERVER}" ]]; then
  PULSE_SERVER=localhost:4713
fi

if [[ -z "${INITIAL_USERNAME}" ]]; then
  INITIAL_USERNAME="user"
fi

if [ ! -f /home/$INITIAL_USERNAME/.xinitrc ]; then
  echo '/usr/bin/twm' > /home/$INITIAL_USERNAME/.xinitrc
fi

if [ ! -x /home/$INITIAL_USERNAME/.xinitrc ]; then
  chmod +x /home/$INITIAL_USERNAME/.xinitrc
fi

/home/$INITIAL_USERNAME/.xinitrc & echo $! > /tmp/xsdl.pidfile
  
while [ ! -f /tmp/xsdl.pidfile ]
do
  sleep 1
done

xterm -geometry 80x24+0+0 -e /bin/bash --login &
