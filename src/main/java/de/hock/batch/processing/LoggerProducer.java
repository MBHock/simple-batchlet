package de.hock.batch.processing;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 *
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class LoggerProducer {

	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

}
