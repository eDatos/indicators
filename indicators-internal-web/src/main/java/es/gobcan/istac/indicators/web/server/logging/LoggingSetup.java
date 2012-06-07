package es.gobcan.istac.indicators.web.server.logging;

import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class LoggingSetup {

    private String logConfigurationFile = null;

    public void setLogConfigurationFile(String logConfigurationFile) {
        this.logConfigurationFile = logConfigurationFile;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        try {
            // CONFIGURAMOS EL LOGBACK
            if (logConfigurationFile == null) {
                throw new RuntimeException("Logging configuration file has not been set \"logConfigurationFile\"");
            }
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(loggerContext);
                loggerContext.reset();

                configurator.doConfigure(logConfigurationFile);
            } catch (JoranException e) {
                throw new RuntimeException("Error configuring logging system", e);
            }
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
}
