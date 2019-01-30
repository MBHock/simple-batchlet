/*
 * Copyright (c) 2019 Bundesagentur fuer Arbeit. All Rights Reserved.
 *
 */
package de.ba.egov.batch.processing;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 *
 * @author <a href="mailto:Mojammal.Hock2@arbeitsagentur.de">Mojammal Hock</a>
 */
public class LoggerProducer {

	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

}
