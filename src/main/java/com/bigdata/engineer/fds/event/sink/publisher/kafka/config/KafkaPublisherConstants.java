package com.bigdata.engineer.fds.event.sink.publisher.kafka.config;

public class KafkaPublisherConstants {
    public static final String LOG_APPENDER = "KAFKA_PUBLISHER_LOG:: ";
    public static final String KAFKA_PROPERTIES_FILE_NAME = "kafka.properties";

    /*	---------------------------------------------------------------------------------------
                                  kafka publisher specific information
        ---------------------------------------------------------------------------------------	*/
    public static final String PUBLISHER_APPLICATION_ID = "kafka.publisher.applicationid";
    public static final String PUBLISHER_BOOTSTRAP = "kafka.publisher.bootstrap";
    public static final String PUBLISHER_TOPIC = "kafka.publisher.topic";
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;

    /*	---------------------------------------------------------------------------------------
                 Default values for the publisher specific configurations listed above
        ---------------------------------------------------------------------------------------	*/
    public static final String DEFAULT_PUBLISHER_APPLICATION_ID = "bank-event-publisher";
    public static final String DEFAULT_PUBLISHER_BOOTSTRAP = "localhost:9092";
    public static final String DEFAULT_PUBLISHER_TOPIC = "bank.events";
}
