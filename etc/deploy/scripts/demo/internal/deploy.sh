#!/bin/bash

HOME_PATH=indicators
TRANSFER_PATH=$HOME_PATH/tmp
SCRIPTS_PATH=$HOME_PATH/scripts
ENV_CONF=$HOME_PATH/env
DEPLOY_TARGET_PATH=/servers/edatos-internal/tomcats/edatos-internal01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/indicators/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy/config/demo/internal/* deploy@estadisticas.arte.internal:$ENV_CONF
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy/utils/utilities.sh deploy@estadisticas.arte.internal:$SCRIPTS_PATH
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" indicators-internal-web/target/indicators-internal-*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/indicators-internal.war
ssh -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" deploy@estadisticas.arte.internal <<EOF

    chmod a+x $SCRIPTS_PATH/utilities.sh;
    . $SCRIPTS_PATH/utilities.sh

    ###
    # INDICATORS-INTERNAL
    ###

    if [ $RESTART -eq 1 ]; then
        sudo service edatos-internal01 stop
        checkPROC "edatos-internal"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-internal
    sudo mv $TRANSFER_PATH/indicators-internal.war $DEPLOY_TARGET_PATH/indicators-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators-internal.war -d $DEPLOY_TARGET_PATH/indicators-internal
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-internal.war

    # Restore Configuration
    sudo mv $ENV_CONF/environment.xml $DEPLOY_TARGET_PATH/indicators-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    # Take care!, it's not necessary to restore the logback.xml file, it's externally configured in METAMAC_COMMON_METADATA database (environment.indicators.data)
    # sudo cp $ENV_CONF/logback.xml $DEPLOY_TARGET_PATH/indicators-internal/$LOGBACK_RELATIVE_PATH_FILE

    sudo chown -R edatos-internal:edatos-internal /servers/edatos-internal
    
    if [ $RESTART -eq 1 ]; then
        sudo service edatos-internal01 start
    fi
    
    sudo rm -rf $SCRIPTS_PATH/*

    echo "Finished deploy"

EOF