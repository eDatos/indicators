#!/bin/bash

TIME=$1
FILE=/servers/edatos-internal/logs/edatos-internal01.log

ssh -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" deploy@estadisticas.arte.internal <<EOF

    # WORKAROUND: The command "timeout" when finished does not output the value zero as exit status, causing the job be marked as error in Gitlab. To avoid this "|| true" is used.
    sudo timeout $TIME tail -f $FILE || true

EOF