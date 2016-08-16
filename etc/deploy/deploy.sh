#!/bin/sh

HOME_PATH=indicators
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/metamac/tomcats/metamac01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/indicators/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml

scp -r etc/deploy deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH
scp indicators-internal-web/target/indicators-internal-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/indicators-internal.war
scp indicators-web/target/indicators-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/indicators-visualizations.war
scp indicators-external-api-web/target/indicators-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/indicators.war
ssh deploy@estadisticas.arte-consultores.com <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    . $TRANSFER_PATH/deploy/utilities.sh
    
    sudo service metamac01 stop
    checkPROC "metamac"
    
    ###
    # INDICATORS-INTERNAL
    ###
    
    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-internal
    sudo mv $TRANSFER_PATH/indicators-internal.war $DEPLOY_TARGET_PATH/indicators-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators-internal.war -d $DEPLOY_TARGET_PATH/indicators-internal
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-internal.war
    
    # Restore Configuration
    sudo cp $HOME_PATH/environment_internal.xml $DEPLOY_TARGET_PATH/indicators-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_internal.xml $DEPLOY_TARGET_PATH/indicators-internal/$LOGBACK_RELATIVE_PATH_FILE
    
    
    ###
    # INDICATORS-EXTERNAL
    ###
    
    # Update Process
    q
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators
    sudo mv $TRANSFER_PATH/indicators.war $DEPLOY_TARGET_PATH/indicators.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators.war -d $DEPLOY_TARGET_PATH/indicators
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators.war
    
    # Restore Configuration
    sudo cp $HOME_PATH/environment.xml $DEPLOY_TARGET_PATH/indicators/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback.xml $DEPLOY_TARGET_PATH/indicators/$LOGBACK_RELATIVE_PATH_FILE


    ###
    # INDICATORS-VISUALIZATIONS
    ###
    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-visualizations
    sudo mv $TRANSFER_PATH/indicators-visualizations.war $DEPLOY_TARGET_PATH/indicators-visualizations.war
    sudo unzip $DEPLOY_TARGET_PATH/indicators-visualizations.war -d $DEPLOY_TARGET_PATH/indicators-visualizations
    sudo rm -rf $DEPLOY_TARGET_PATH/indicators-visualizations.war
    
    # Restore Configuration
    sudo cp $HOME_PATH/environment_visualizations.xml $DEPLOY_TARGET_PATH/indicators-visualizations/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_visualizations.xml $DEPLOY_TARGET_PATH/indicators-visualizations/$LOGBACK_RELATIVE_PATH_FILE

    
    sudo chown -R metamac.metamac /servers/metamac
    sudo service metamac01 start
    checkURL "http://estadisticas.arte-consultores.com/indicators-internal/" "metamac01"
    checkURL "http://estadisticas.arte-consultores.com/indicators/v1.0/~latest" "metamac01"
    checkURL "http://estadisticas.arte-consultores.com/indicators-visualizations/indicatorsSystems" "metamac01"

EOF
