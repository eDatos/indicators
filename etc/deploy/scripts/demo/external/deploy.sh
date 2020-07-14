#!/bin/bash

HOME_PATH=indicators
TRANSFER_PATH=$HOME_PATH/tmp
SCRIPTS_PATH=$HOME_PATH/scripts
ENV_CONF=$HOME_PATH/env
DEPLOY_TARGET_PATH=/servers/edatos-external/tomcats/edatos-external01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/indicators/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy/config/demo/external/* deploy@estadisticas.arte.internal:$ENV_CONF
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy/utils/utilities.sh deploy@estadisticas.arte.internal:$SCRIPTS_PATH
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" indicators-web/target/indicators-*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/indicators-visualizations.war
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" indicators-external-api-web/target/indicators-*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/indicators.war
ssh -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" deploy@estadisticas.arte.internal <<EOF

    chmod a+x $SCRIPTS_PATH/utilities.sh;
    . $SCRIPTS_PATH/utilities.sh

    ###
    # INDICATORS-EXTERNAL
    ###
            
    if [ $RESTART -eq 1 ]; then
        sudo service edatos-external01 stop
        checkPROC "edatos-external"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators
    sudo mv $TRANSFER_PATH/indicators.war $DEPLOY_TARGET_PATH/indicators.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators.war -d $DEPLOY_TARGET_PATH/indicators
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators.war

    # Restore Configuration
    sudo mv $ENV_CONF/environment.xml $DEPLOY_TARGET_PATH/indicators/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo mv $ENV_CONF/logback.xml $DEPLOY_TARGET_PATH/indicators/$LOGBACK_RELATIVE_PATH_FILE


    ###
    # INDICATORS-VISUALIZATIONS
    ###
    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-visualizations
    sudo mv $TRANSFER_PATH/indicators-visualizations.war $DEPLOY_TARGET_PATH/indicators-visualizations.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators-visualizations.war -d $DEPLOY_TARGET_PATH/indicators-visualizations
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-visualizations.war

    # Restore Configuration
    sudo mv $ENV_CONF/environment_visualizations.xml $DEPLOY_TARGET_PATH/indicators-visualizations/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo mv $ENV_CONF/logback_visualizations.xml $DEPLOY_TARGET_PATH/indicators-visualizations/$LOGBACK_RELATIVE_PATH_FILE

    sudo chown -R edatos-external:edatos-external /servers/edatos-external

    if [ $RESTART -eq 1 ]; then
        sudo service edatos-external01 start
    fi
    
    sudo rm -rf $SCRIPTS_PATH/*

    echo "Finished deploy"

EOF
