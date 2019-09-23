package de.hock.batch.processing;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named(value = "TestBatchlet")
public class TestBatchlet implements Batchlet {

    private static final int BLOCK_SIZE = 2000;
    private static final String OUT_FILENAME = "outputDirectory";
    private static final String NUMBER_OF_LINES = "numberOfLines";

    private static final LocalDateTime datetime = LocalDateTime.now();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter filenameformatter = DateTimeFormatter.ofPattern("yyyMMdd_HHmmss_SSS");

    private static final Random random = new Random();
    private static final Integer lowerBound = 1;
    private static final Integer higherBound = 100;
    private BufferedWriter writer = null;
    private AtomicInteger fileNumber = new AtomicInteger(0);

    @Inject
    private JobContext jobContext;

    @Inject
    private Logger logger;

    @Inject
    @BatchProperty(name = NUMBER_OF_LINES)
    private Integer lineNumbers;

    @Inject
    @BatchProperty(name = "outputFilename")
    private String fileName;

    @Override
    public String process() throws IOException {
        String lineNumbers = jobContext.getProperties().getProperty(NUMBER_OF_LINES, "4000");
        logger.log(Level.INFO, "Write {0} lines in file ...", lineNumbers);

        int counter = 0;
        int limit = Integer.parseInt(lineNumbers);

        while(counter < limit) {
            openWriter();

            writeData(counter);

            counter++;
        }

        closeBufferedWriter();
        logger.log(Level.INFO, "{0} line(s) has been written on file", counter);

        return BatchStatus.COMPLETED.name();

    }

    private void openWriter() throws IOException {
        if(writer == null) {
            writer = new BufferedWriter(new FileWriter(new File(fileName)));
        }
    }

    private void writeData(int counter) throws IOException {

        int nextNumber = random.nextInt(higherBound - lowerBound) + lowerBound;
        String testdata = String.format("%s%n", "");
        if(nextNumber > 20) {
            testdata = String.format("%s:   current line number is %d.%n", LocalDateTime.now().format(formatter),
                    counter);
        }

        writer.write(testdata);
    }

    private void closeBufferedWriter() throws IOException {
        if(writer != null) {
            writer.close();
        }
    }

    private synchronized File getOutfilename() {
        File file = new File(jobContext.getProperties().getProperty(OUT_FILENAME),
                "sampleout_" + datetime.format(filenameformatter) + ".txt");

        if(file.exists()) {
            file = new File(jobContext.getProperties().getProperty(OUT_FILENAME),
                    "sampleout_" + datetime.format(filenameformatter) + "_" + fileNumber.incrementAndGet() + ".txt");
        }

        logger.log(Level.INFO, "The {0} file is created in the configured directory", file.getAbsolutePath());
        return file;
    }

    @Override
    public void stop() throws Exception {
        // do nothing
        if(writer != null) {
            writer.close();
        }
    }

}
