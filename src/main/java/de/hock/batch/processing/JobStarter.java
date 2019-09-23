package de.hock.batch.processing;

import org.jberet.runtime.JobExecutionImpl;
import org.jberet.se._private.SEBatchLogger;
import org.jberet.se._private.SEBatchMessages;

import javax.batch.operations.BatchRuntimeException;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class JobStarter {

    public static void main(final String[] args) throws BatchRuntimeException {
//        if (args.length == 0) {
//            usage(args);
//            return;
//        }
//        final String jobXml = args[0];
//        if (jobXml == null || jobXml.isEmpty()) {
//            usage(args);
//            return;
//        }

        Instant jobStart = Instant.now();
        final java.util.Properties jobParameters = new java.util.Properties();
        for(int i = 1; i < args.length; i++) {
            final int equalSignPos = args[i].indexOf('=');
            if(equalSignPos <= 0) {
                usage(args);
                return;
            }
            final String key = args[i].substring(0, equalSignPos).trim();
            final String val = args[i].substring(equalSignPos + 1).trim();
            System.out.println(String.format("prop: '%s' -> '%s'", key, val));
            jobParameters.setProperty(key, val);
        }

        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        final long jobExecutionId;
        try {
            jobExecutionId = jobOperator.start("batchlet-example", jobParameters);
            final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
            jobExecution.awaitTermination(0, TimeUnit.SECONDS);  //no timeout

            if(!jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
                throw SEBatchMessages.MESSAGES.jobDidNotComplete("batchlet-example",
                        jobExecution.getBatchStatus(), jobExecution.getExitStatus());
            }
        }
        catch(InterruptedException e) {
            throw new BatchRuntimeException(e);
        }

        Instant jobEnd = Instant.now();
        Duration between = Duration.between(jobStart, jobEnd);
        System.out.println(String.format("Total job execution time: %s", between));
    }

    private static void usage(final String[] args) {
        SEBatchLogger.LOGGER.usage(args);
    }

//    public static void main(String[] args) {
//
//        String[] jobargs = new String[1 + args.length];
//        int index = 0;
//        jobargs[index++] = "batchlet-example";
//
//        for(int i = 0; i < args.length; i++) {
//            jobargs[index++] = args[i];
//        }
//
//        org.jberet.se.Main.main(args);
//
//
//
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            public void run() {
//                System.out.println("Shutdown Hook is running !");
//            }
//        });
//
//        Properties prop = new Properties();
//
//        JobOperator jobOperator = BatchRuntime.getJobOperator();
//        Long executionId = jobOperator.start("batchlet-example", prop);
//
//        JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(executionId);
//        String exitStatus = jobExecution.getExitStatus();
//        System.out.println("Job exit status: " + exitStatus);
////        System.out.println("Application Terminating ...");
//        System.exit(Integer.parseInt(exitStatus));
//    }

}
