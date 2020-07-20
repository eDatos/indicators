#!/bin/bash

TIME=$1
FILE=/servers/guagua/logs/catalina.out

ssh -o "StrictHostKeyChecking=no" deploy@mgdelicias.gobiernodecanarias.net <<EOF

    # WORKAROUND: The command "timeout" when finished does not output the value zero as exit status, causing the job be marked as error in Gitlab. To avoid this "|| true" is used.
    sudo timeout $TIME tail -f $FILE || true

EOF