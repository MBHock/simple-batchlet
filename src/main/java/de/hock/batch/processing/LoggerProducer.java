package de.hock.batch.processing;

import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import javax.batch.api.BatchProperty;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class LoggerProducer {

    public static final String LOGGING_PROPERTIES = "logging.properties";

    @BatchProperty(name = "logDirectory")
    private String logDirecotry;

    @PostConstruct
    void initializeLoggingConfiguration() {
        try {
            LogManager.getLogManager().readConfiguration(getResourceAsStream(LOGGING_PROPERTIES));
        }
        catch(IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error while reading logging property from {0}", LOGGING_PROPERTIES);
            throw new RuntimeException(e);
        }
    }

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    private InputStream getResourceAsStream(String propertyFileName) {
        InputStream is = LoggerProducer.class.getClassLoader().getResourceAsStream(propertyFileName);
        Objects.requireNonNull(is, () -> String.format("The property file %s expected in the %s directory, but is missing", propertyFileName, "src/main/resources"));
        return is;
    }

}
