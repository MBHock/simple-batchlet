package de.hock.batch.processing;

import javax.annotation.PostConstruct;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.hock.batch.processing.BatchletProperties.END_EXCLUSIVE;
import static de.hock.batch.processing.BatchletProperties.OUTPUT_FILENAME;
import static de.hock.batch.processing.BatchletProperties.START_INCLUSIVE;

@Named
public class SimplePartitionMapper implements PartitionMapper {

    @Inject
    private BatchletProperties batchletProperties;

    @Inject
    private Logger logger;

    private Integer numberOfPartition;
    private String outputDirectory;
    private Integer numberOfLines;

    @PostConstruct
    void initProperties() {
        numberOfPartition = batchletProperties.getNumberOfFiles();
        outputDirectory = batchletProperties.getOutputDirectory();
        numberOfLines = batchletProperties.getNumberOfLines();

        logger.log(Level.INFO, "Job properties: NumberOfPartition={0}, NumberOfLines={1}, OutputDirectory={2}", new Object[]{numberOfPartition, numberOfLines, outputDirectory});
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        PartitionPlan partitionPlan = new PartitionPlanImpl();

        partitionPlan.setPartitions(numberOfPartition);
        partitionPlan.setThreads(batchletProperties.getThreadCount());
        partitionPlan.setPartitionProperties(createPartitionProperties());

        logger.log(Level.INFO, "Partition plan: NumberOfPartitions={0}, NumberOfThreads={1}, NumberOfProperties={2}", new Object[]{partitionPlan.getPartitions(), partitionPlan.getThreads(), partitionPlan.getPartitionProperties().length});
        return partitionPlan;
    }

    private Properties[] createPartitionProperties() {
        Properties[] props = new Properties[numberOfPartition];
        for(int index = 0; index < numberOfPartition; index++) {
            Path path = Paths.get(outputDirectory, "sampleoutput_" + index + ".txt");
            Properties properties = new Properties();
            properties.setProperty(OUTPUT_FILENAME, path.toFile().toString());
            properties.setProperty(START_INCLUSIVE, String.valueOf(index * numberOfLines));
            properties.setProperty(END_EXCLUSIVE, String.valueOf((index + 1) * numberOfLines));
            props[index] = properties;
        }

        return props;
    }

}
