package de.hock.batch.processing;

import javax.annotation.PostConstruct;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@Named
public class SimplePartitionMapper implements PartitionMapper {

    public static final String OUTPUT_FILENAME = "outputFilename";

    @Inject
    private JobContext jobContext;

    //    @Inject
//    @BatchProperty(name = "numberOfPartitions")
    private Integer numberOfPartition;

    //    @Inject
//    @BatchProperty(name = "outputDirectory")
    private String outputDirectory;

    @PostConstruct
    void initProperties() {
        String partitions = jobContext.getProperties().getProperty("numberOfPartitions", "1");
        numberOfPartition = Integer.parseInt(partitions);

        outputDirectory = jobContext.getProperties().getProperty("outputDirectory");
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        PartitionPlan partitionPlan = new PartitionPlanImpl();

        numberOfPartition = Objects.isNull(numberOfPartition) ? 1 : numberOfPartition;
        partitionPlan.setPartitions(numberOfPartition);
        partitionPlan.setPartitionProperties(createPartitionProperties());

        return partitionPlan;
    }

    private Properties[] createPartitionProperties() {
        Properties[] props = new Properties[numberOfPartition];
        for(int index = 0; index < numberOfPartition; index++) {
            Path path = Paths.get(outputDirectory, "sampleoutput_" + index + ".txt");
            Properties properties = new Properties();
            properties.setProperty(OUTPUT_FILENAME, path.toFile().toString());
            props[index] = properties;
        }
        return props;
    }

}
