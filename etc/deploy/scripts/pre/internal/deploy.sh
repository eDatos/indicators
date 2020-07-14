#!/bin/bash

HOME_PATH=indicators
TRANSFER_PATH=$HOME_PATH/tmp
SCRIPTS_PATH=$HOME_PATH/scripts
ENV_CONF=$HOME_PATH/env
DEPLOY_TARGET_PATH=/servers/arveja/tomcats/arveja01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/indicators/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o "StrictHostKeyChecking=no" -r etc/deploy/config/pre/internal/* deploy@mgcarta.gobiernodecanarias.net:$ENV_CONF
scp -o "StrictHostKeyChecking=no" -r etc/deploy/utils/utilities.sh deploy@mgcarta.gobiernodecanarias.net:$SCRIPTS_PATH
scp -o "StrictHostKeyChecking=no" indicators-internal-web/target/indicators-internal-*.war deploy@mgcarta.gobiernodecanarias.net:$TRANSFER_PATH/indicators-internal.war
ssh -o "StrictHostKeyChecking=no" deploy@mgcarta.gobiernodecanarias.net <<EOF

    chmod a+x $SCRIPTS_PATH/utilities.sh;
    . $SCRIPTS_PATH/utilities.sh

    ###
    # INDICATORS-INTERNAL
    ###

    if [ $RESTART -eq 1 ]; then
        sudo service arveja01 stop
        checkPROC "arveja"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-internal-istac
    sudo mv $TRANSFER_PATH/indicators-internal.war $DEPLOY_TARGET_PATH/indicators-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators-internal.war -d $DEPLOY_TARGET_PATH/indicators-internal-istac
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-internal.war

    # Restore Configuration
    sudo mv $ENV_CONF/environment.xml $DEPLOY_TARGET_PATH/indicators-internal-istac/$ENVIRONMENT_RELATIVE_PATH_FILE
    # Take care!, it's not necessary to restore the logback.xml file, it's externally configured in METAMAC_COMMON_METADATA database (environment.indicators.data)
    # sudo cp $ENV_CONF/logback.xml $DEPLOY_TARGET_PATH/indicators-internal-istac/$LOGBACK_RELATIVE_PATH_FILE
    
    sudo chown -R arveja:arveja /servers/arveja
    
    if [ $RESTART -eq 1 ]; then
        sudo service arveja01 start
    fi
    
    sudo rm -rf $SCRIPTS_PATH/*

    echo "Finished deploy"

EOF