package com.bigdata.engineer.fds.event.source.consumer.config;

public class KafkaConsumerConstants {
    public static final String LOG_APPENDER = "FRAUD_DETECTOR_LOG:: ";
    public static final String KAFKA_PROPERTIES_FILE_NAME = "kafka.properties";

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
    public static final String RULEPROCESS2 = "ruleProcess2";
    public static final String KAFKASINK = "kafkaSink";


    /*	---------------------------------------------------------------------------------------
                 Default values for the Consumer specific configurations listed above
        ---------------------------------------------------------------------------------------	*/
    public static final String DEFAULT_CONSUMER_APPLICATION_ID = "fraud-detection-consumer";
    public static final String DEFAULT_CONSUMER_BOOTSTRAP = "localhost:9092";
    public static final String DEFAULT_CONSUMER_GROUP = "test-consumer-group";
    public static final String DEFAULT_CONSUMER_TOPIC = "bank.events";
    public static final String DEFAULT_CONSUMER_AUTO_OFFSET_RESET = "earliest";
}
