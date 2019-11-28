#! /bin/bash

if [[ -z "${INITIAL_USERNAME}" ]]; then
  INITIAL_USERNAME="user"
fi

su $INITIAL_USERNAME -c /support/startXSDLServerStep2.sh
