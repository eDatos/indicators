#!/bin/bash

if [[ $# -ne 2 ]] ; then
    echo 'Ejemplo de uso: server.sh start edatos-internal'
    echo 'Ejemplo de uso: server.sh stop edatos-external'
    exit 1
fi

ACTION=$1
SERVER=$2

ssh -o "StrictHostKeyChecking=no" deploy@mgcartera.gobiernodecanarias.net <<EOF

    sudo systemctl $ACTION $SERVER

EOF