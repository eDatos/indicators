#!/bin/bash

HOME_PATH=indicators
TRANSFER_PATH=$HOME_PATH/tmp
SCRIPTS_PATH=$HOME_PATH/scripts
ENV_CONF=$HOME_PATH/env
DEPLOY_TARGET_PATH=/servers/frangollo/tomcats/frangollo01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/indicators/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o "StrictHostKeyChecking=no" -r etc/deploy/config/pre/external/* deploy@mgcartera.gobiernodecanarias.net:$ENV_CONF
scp -o "StrictHostKeyChecking=no" -r etc/deploy/utils/utilities.sh deploy@mgcartera.gobiernodecanarias.net:$SCRIPTS_PATH
scp -o "StrictHostKeyChecking=no" indicators-web/target/indicators-*.war deploy@mgcartera.gobiernodecanarias.net:$TRANSFER_PATH/indicators-visualizations.war
scp -o "StrictHostKeyChecking=no" indicators-external-api-web/target/indicators-*.war deploy@mgcartera.gobiernodecanarias.net:$TRANSFER_PATH/indicators.war
ssh -o "StrictHostKeyChecking=no" deploy@mgcartera.gobiernodecanarias.net <<EOF

    chmod a+x $SCRIPTS_PATH/utilities.sh;
    . $SCRIPTS_PATH/utilities.sh

    ###
    # INDICATORS-EXTERNAL
    ###
            
    if [ $RESTART -eq 1 ]; then
        sudo service frangollo01 stop
        checkPROC "frangollo"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/api#estadisticas#indicators
    sudo mv $TRANSFER_PATH/indicators.war $DEPLOY_TARGET_PATH/indicators.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators.war -d $DEPLOY_TARGET_PATH/api#estadisticas#indicators
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators.war

    # Restore Configuration
    sudo mv $ENV_CONF/environment.xml $DEPLOY_TARGET_PATH/api#estadisticas#indicators/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo mv $ENV_CONF/logback.xml $DEPLOY_TARGET_PATH/api#estadisticas#indicators/$LOGBACK_RELATIVE_PATH_FILE


    ###
    # INDICATORS-VISUALIZATIONS
    ###
    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/istac#indicators-visualizations
    sudo mv $TRANSFER_PATH/indicators-visualizations.war $DEPLOY_TARGET_PATH/indicators-visualizations.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators-visualizations.war -d $DEPLOY_TARGET_PATH/istac#indicators-visualizations
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-visualizations.war

    # Restore Configuration
    sudo mv $ENV_CONF/environment_visualizations.xml $DEPLOY_TARGET_PATH/istac#indicators-visualizations/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo mv $ENV_CONF/logback_visualizations.xml $DEPLOY_TARGET_PATH/istac#indicators-visualizations/$LOGBACK_RELATIVE_PATH_FILE

    sudo chown -R frangollo:frangollo /servers/frangollo

    if [ $RESTART -eq 1 ]; then
        sudo service frangollo01 start
    fi
    
    sudo rm -rf $SCRIPTS_PATH/*

    echo "Finished deploy"

EOF
