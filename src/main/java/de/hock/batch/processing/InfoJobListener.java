package de.hock.batch.processing;

import javax.batch.api.listener.JobListener;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("InfoJobListener")
public class InfoJobListener implements JobListener {

    @Inject
    private Logger logger;

    @Override
    public void beforeJob() throws Exception {
        logger.log(Level.INFO, "The job is starting");
    }

    @Override
    public void afterJob() throws Exception {
        logger.log(Level.INFO, "The job is finished");
    }

}