package com.bigdata.engineer.system.fds.config;

public class KafkaConsumerConstants {
    public static final String LOG_APPENDER = "FRAUD_DETECTOR_LOG:: ";
    public static final String KAFKA_PROPERTIES_FILE_NAME = "kafka.consumer.properties";

    /*	---------------------------------------------------------------------------------------
                                  kafka consumer specific information
        ---------------------------------------------------------------------------------------	*/
    public static final String CONSUMER_APPLICATION_ID = "kafka.consumer.applicationid";
    public static final String CONSUMER_BOOTSTRAP = "kafka.consumer.bootstrap";
    public static final String CONSUMER_GROUP = "kafka.consumer.group";
    public static final String CONSUMER_TOPIC = "kafka.consumer.topic";
    public static final String CONSUMER_AUTO_OFFSET_RESET = "kafka.consumer.offset.reset";


    /*	---------------------------------------------------------------------------------------
                                  kafka stream specific information
        ---------------------------------------------------------------------------------------	*/
    public static final String KAFKASOURCE = "kafkaSource";
    public static final String STOREPROCESS = "storeProcess";
    public static final String RULEPROCESS = "ruleProcess";
    public static final String KAFKASINK = "kafkaSink";

    public static final String FRAUDSTORE_NEWACCOUNT = "FraudStore.NewAccountEvent";
    public static final String FRAUDSTORE_DEPOSITEVENT = "FraudStore.DepositEvent";
    public static final String FRAUDSTORE_WITHDRAWEVENT = "FraudStore.WithdrawEvent";
    public static final String FRAUDSTORE_TRANSFEREVENT = "FraudStore.TransferEvent";
    public static final String FRAUDSTORE_FRAUDDETECTIONS = "FraudStore.FraudDetections";


    /*	---------------------------------------------------------------------------------------
                 Default values for the Consumer specific configurations listed above
        ---------------------------------------------------------------------------------------	*/
    public static final String DEFAULT_CONSUMER_APPLICATION_ID = "fraud-detection-consumer";
    public static final String DEFAULT_CONSUMER_BOOTSTRAP = "localhost:9092";
    public static final String DEFAULT_CONSUMER_GROUP = "test-consumer-group";
    public static final String DEFAULT_CONSUMER_TOPIC = "bank.events";
    public static final String DEFAULT_CONSUMER_AUTO_OFFSET_RESET = "earliest";
}
