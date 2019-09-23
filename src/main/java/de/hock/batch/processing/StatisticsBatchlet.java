package de.hock.batch.processing;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named(value = "StatisticsBatchlet")
public class StatisticsBatchlet extends AbstractBatchlet {

    @Inject
    private StepContext stepContext;

    @Inject
    private Logger logger;
    private Function<Metric, String> mapper = metric -> String.join(" = ", metric.getType().name(), String.valueOf(metric.getValue()));


    @Override
    public String process() throws Exception {
        Metric[] metrics = stepContext.getMetrics();
        logger.log(Level.INFO, "\n*** Statistics *** \n{0}\n******", Stream.of(metrics).collect(Collectors.mapping(mapper, Collectors.joining(System.lineSeparator()))));
        return BatchStatus.COMPLETED.name();
    }

}
