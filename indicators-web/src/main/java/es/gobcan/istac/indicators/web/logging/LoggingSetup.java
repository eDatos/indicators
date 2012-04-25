package es.gobcan.istac.indicators.web.logging;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Log4jConfigurer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;


public class LoggingSetup {
    private Resource logConfigurationFile = null;

    public void setLogConfigurationFile(Resource logFile) {
        this.logConfigurationFile = logFile;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        try {
            // CONFIGURAMOS EL LOGBACK
            if (logConfigurationFile == null) {
                throw new RuntimeException("Logging configuration file has not been set \"logConfigurationFile\"");
            }
            LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(loggerContext);
                loggerContext.reset();
                configurator.doConfigure(logConfigurationFile.getInputStream());
            } catch (JoranException e) {
                throw new RuntimeException("Error configuring logging system",e);
            }
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }

}
