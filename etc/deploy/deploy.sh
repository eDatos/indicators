#!/bin/bash

HOME_PATH=indicators
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH_EXTERNAL=/servers/edatos-external/tomcats/edatos-external01/webapps
DEPLOY_TARGET_PATH_INTERNAL=/servers/edatos-internal/tomcats/edatos-internal01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/indicators/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/logback.xml
RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy deploy@estadisticas.arte.internal:$TRANSFER_PATH
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" indicators-internal-web/target/indicators-internal-*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/indicators-internal.war
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" indicators-web/target/indicators-*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/indicators-visualizations.war
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" indicators-external-api-web/target/indicators-*.war deploy@estadisticas.arte.internal:$TRANSFER_PATH/indicators.war
ssh -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" deploy@estadisticas.arte.internal <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    . $TRANSFER_PATH/deploy/utilities.sh

    ###
    # INDICATORS-INTERNAL
    ###
    
    if [ $RESTART -eq 1 ]; then
        sudo service edatos-internal01 stop
        checkPROC "edatos-internal"
    fi
    
    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal
    sudo mv $TRANSFER_PATH/indicators-internal.war $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal.war
    sudo unzip $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal.war -d $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal
    sudo rm -rf $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment_internal.xml $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    # Take care!, it's not necessary to restore the logback.xml file, it's externally configured in METAMAC_COMMON_METADATA database (environment.indicators.data)
    # sudo cp $HOME_PATH/logback_internal.xml $DEPLOY_TARGET_PATH_INTERNAL/indicators-internal/$LOGBACK_RELATIVE_PATH_FILE
    
    if [ $RESTART -eq 1 ]; then
        sudo chown -R edatos-internal.edatos-internal /servers/edatos-internal     
        sudo service edatos-internal01 start
    fi

    ###
    # INDICATORS-EXTERNAL
    ###
            
    if [ $RESTART -eq 1 ]; then
        sudo service edatos-external01 stop
        checkPROC "edatos-external"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH_EXTERNAL/indicators
    sudo mv $TRANSFER_PATH/indicators.war $DEPLOY_TARGET_PATH_EXTERNAL/indicators.war
    sudo unzip $DEPLOY_TARGET_PATH_EXTERNAL/indicators.war -d $DEPLOY_TARGET_PATH_EXTERNAL/indicators
    sudo rm -rf $DEPLOY_TARGET_PATH_EXTERNAL/indicators.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment_external.xml $DEPLOY_TARGET_PATH_EXTERNAL/indicators/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_external.xml $DEPLOY_TARGET_PATH_EXTERNAL/indicators/$LOGBACK_RELATIVE_PATH_FILE


    ###
    # INDICATORS-VISUALIZATIONS
    ###
    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations
    sudo mv $TRANSFER_PATH/indicators-visualizations.war $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations.war
    sudo unzip $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations.war -d $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations
    sudo rm -rf $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment_external_visualizations.xml $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/logback_external_visualizations.xml $DEPLOY_TARGET_PATH_EXTERNAL/indicators-visualizations/$LOGBACK_RELATIVE_PATH_FILE

    if [ $RESTART -eq 1 ]; then
        sudo chown -R edatos-external.edatos-external /servers/edatos-external        
        sudo service edatos-external01 start
    fi

    echo "Finished deploy"
EOF
