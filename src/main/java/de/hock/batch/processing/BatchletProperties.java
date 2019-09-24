package de.hock.batch.processing;

import org.jberet.runtime.context.JobContextImpl;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Properties;

public final class BatchletProperties {

    @Inject
    private JobContext jobContext;

    public static final String OUTPUT_DIRECTORY = "outputDirectory";
    public static final String START_INCLUSIVE = "startInclusive";
    public static final String END_EXCLUSIVE = "endExclusive";
    public static final String OUTPUT_FILENAME = "outputFilename";
    public static final String NUMBER_OF_FILES = "numberOfFiles";
    public static final String NUMBER_OF_LINES = "numberOfLines";
    public static final String THREAD_COUNT = "threadCount";

    public Integer getNumberOfFiles() {

        String numOfFiles = Objects.isNull(getJobParameter(NUMBER_OF_FILES)) ? getProperty(NUMBER_OF_FILES) : getJobParameter(NUMBER_OF_FILES);
        numOfFiles = Objects.isNull(numOfFiles) ? "1" : numOfFiles;

        return Integer.parseInt(numOfFiles);
    }

    public String getOutputDirectory() {
        return jobContext.getProperties().getProperty(OUTPUT_DIRECTORY);
    }

    public Integer getNumberOfLines() {
        String numOfLines = Objects.isNull(getJobParameter(NUMBER_OF_LINES)) ? getProperty(NUMBER_OF_LINES) : getJobParameter(NUMBER_OF_LINES);
        numOfLines = Objects.isNull(numOfLines) ? "1000" : numOfLines;

        return Integer.parseInt(numOfLines);
    }

    public Integer getThreadCount() {
        String threadCount = Objects.isNull(getJobParameter(THREAD_COUNT)) ? getProperty(THREAD_COUNT) : getJobParameter(THREAD_COUNT);
        threadCount = Objects.isNull(threadCount) ? "5" : threadCount;

        return Integer.parseInt(threadCount);
    }

    private String getProperty(String key) {
        return jobContext.getProperties().getProperty(key);
    }

    private String getJobParameter(String key) {
        if(jobContext instanceof JobContextImpl) {
            Properties jobParameters = ((JobContextImpl) jobContext).getJobParameters();
            return jobParameters.getProperty(key);
        }

        return null;
    }

}
