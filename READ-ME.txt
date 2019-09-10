To run the batch, just execute the following command:

  mvn clean install exec:java -Dexec.arguments="batchlet-example"
  or
  mvn exec:java -Dexec.arguments="batchlet-example"
     
	  where as "batchlet-example" is the name of job XML, which lies in the class path.

To supply additional parameters in the batch execution, the command should be as follows:
mvn exec:java -Dexec.arguments="batchlet-example jobParam1=x jobParam2=y jobParam3=z"

