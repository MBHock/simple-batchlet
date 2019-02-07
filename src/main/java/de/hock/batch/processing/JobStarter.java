package de.hock.batch.processing;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;

import org.jberet.runtime.JobExecutionImpl;

/**
 *
 *
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class JobStarter {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		Properties prop = new Properties();

		JobOperator jobOperator = BatchRuntime.getJobOperator();
		Long executionId = jobOperator.start("batchlet-example", prop);

		JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(executionId);
		String exitStatus = jobExecution.getExitStatus();
		System.out.println("Job exit status: " + exitStatus);
		System.exit(Integer.parseInt(exitStatus));
	}
}
