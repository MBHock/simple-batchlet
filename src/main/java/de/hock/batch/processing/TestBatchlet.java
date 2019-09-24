package de.hock.batch.processing;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.batch.operations.BatchRuntimeException;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static de.hock.batch.processing.BatchletProperties.END_EXCLUSIVE;
import static de.hock.batch.processing.BatchletProperties.OUTPUT_FILENAME;
import static de.hock.batch.processing.BatchletProperties.START_INCLUSIVE;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named(value = "TestBatchlet")
public class TestBatchlet implements Batchlet {

//    private static final int BLOCK_SIZE = 2000;
//    private static final DateTimeFormatter filenameformatter = DateTimeFormatter.ofPattern("yyyMMdd_HHmmss_SSS");
//    private static final LocalDateTime datetime = LocalDateTime.now();
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//    private AtomicInteger fileNumber = new AtomicInteger(0);
//    @Inject
//    private JobContext jobContext;

    private static final Random random = new Random();
    private static final Integer lowerBound = 1;
    private static final Integer higherBound = 100;
    private BufferedWriter writer = null;

    @Inject
    private Logger logger;

    @Inject
    @BatchProperty(name = START_INCLUSIVE)
    private Integer startInclusive;

    @Inject
    @BatchProperty(name = END_EXCLUSIVE)
    private Integer endExclusive;

    @Inject
    @BatchProperty(name = OUTPUT_FILENAME)
    private String fileName;

    @Override
    public String process() throws IOException {
        logger.log(Level.INFO, "Dummy lines {0} to {1} will be written in file {2}", new Object[]{startInclusive, endExclusive, fileName});

        openWriter();
        IntStream.range(startInclusive, endExclusive).boxed().forEach(this::writeData);
        closeWriter();

        logger.log(Level.FINE, String.format("%d number of line(s) has been written on file %s", Files.readAllLines(Paths.get(fileName)).size(), fileName));
        return BatchStatus.COMPLETED.name();

    }

    private void openWriter() throws IOException {
        if(writer == null) {
            writer = new BufferedWriter(new FileWriter(new File(fileName)));
        }
    }

    private void writeData(Integer counter) {
//        int nextNumber = random.nextInt(higherBound - lowerBound) + lowerBound;
//        if(nextNumber > 20) {
//        }

        try {
            LocalDateTime now = LocalDateTime.now();
            writer.write(String.format("[%tF %tT]\twriting dummy line number (%06d).%n", now, now, counter));
            if(counter % 100 == 0) { // write an empty line
                writer.write(String.format("%s%n", ""));
            }
        }
        catch(IOException e) {
            throw new BatchRuntimeException(e);
        }
    }

    private void closeWriter() throws IOException {
        if(writer != null) {
            writer.close();
        }
    }

//    private synchronized File getOutfilename() {
//        File file = new File(jobContext.getProperties().getProperty(OUT_FILENAME),
//                "sampleout_" + datetime.format(filenameformatter) + ".txt");
//
//        if(file.exists()) {
//            file = new File(jobContext.getProperties().getProperty(OUT_FILENAME),
//                    "sampleout_" + datetime.format(filenameformatter) + "_" + fileNumber.incrementAndGet() + ".txt");
//        }
//
//        logger.log(Level.INFO, "The {0} file is created in the configured directory", file.getAbsolutePath());
//        return file;
//    }

    @Override
    public void stop() throws Exception {
        if(writer != null) {
            writer.close();
        }
    }
}
