package com.bigdata.engineer.fds.event.source.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class KafkaConfigOperations {
    private static final Logger logger = LogManager.getLogger(KafkaConfigOperations.class);

//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConcurrency(1);
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//
//    public ConsumerFactory<String, String> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerProps(), stringKeyDeserializer(), payloadJsonValueDeserializer());
//    }

    public static Properties consumerProps() {
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, readKafkaConfigs().getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, readKafkaConfigs().getBootstrap());
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, readKafkaConfigs().getOffsetReset());
        return props;

        //        Properties props = new Properties();

//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount-processor");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    private static KafkaConsumerConfiguration readKafkaConfigs() {
        String propertiesFileName = KafkaConsumerConstants.KAFKA_PROPERTIES_FILE_NAME;
        KafkaConsumerConfiguration configuration = new KafkaConsumerConfiguration();
        Properties properties = new Properties();
        InputStream propertiesInputStream = null;

        try {
            ClassLoader loader = KafkaConfigOperations.class.getClassLoader();
            URL path = loader.getResource(propertiesFileName);
            String root = path.getPath()
                    .replace("fraud-detection-system.jar!/kafka.properties","")
                    .replace("jar:", "")
                    .replace("file:", "");

            configuration.setRootPath(root);

            //in oder to deploy the jar, this procedure detects system root
            if(root.endsWith("/")) {
                propertiesInputStream = new FileInputStream(root + propertiesFileName);
                if (logger.isDebugEnabled()) {
                    logger.debug(KafkaConsumerConstants.LOG_APPENDER + "Property path: {}", root + propertiesFileName);
                }
            } else {
                propertiesInputStream = new FileInputStream(root);
                logger.info(KafkaConsumerConstants.LOG_APPENDER + "Property path: {}", root);
            }

            //load a properties file from class path, inside static method
            properties.load(propertiesInputStream);

            //start Cousumer Configuration
            configuration.setApplicationId(properties.getProperty(KafkaConsumerConstants.CONSUMER_APPLICATION_ID));
            configuration.setBootstrap(properties.getProperty(KafkaConsumerConstants.CONSUMER_BOOTSTRAP));
            configuration.setGroup(properties.getProperty(KafkaConsumerConstants.CONSUMER_GROUP));
            configuration.setTopic(properties.getProperty(KafkaConsumerConstants.CONSUMER_TOPIC));
            configuration.setOffsetReset(properties.getProperty(KafkaConsumerConstants.CONSUMER_AUTO_OFFSET_RESET));

            logger.info(KafkaConsumerConstants.LOG_APPENDER + "Application ID: {}", configuration.getApplicationId());
            logger.info(KafkaConsumerConstants.LOG_APPENDER + "Bootstrap: {}", configuration.getBootstrap());
            logger.info(KafkaConsumerConstants.LOG_APPENDER + "Consumer Group: {}", configuration.getGroup());
            logger.info(KafkaConsumerConstants.LOG_APPENDER + "Topic: {}", configuration.getTopic());
            logger.info(KafkaConsumerConstants.LOG_APPENDER + "Offset Reset: {}", configuration.getOffsetReset());

        } catch (FileNotFoundException ex) {
            logger.error(KafkaConsumerConstants.LOG_APPENDER + "Unable to find {} file at: {}", propertiesFileName, propertiesFileName);
            configuration = setDefaultConsumerConfigs();
        } catch (IOException ex) {
            logger.error(KafkaConsumerConstants.LOG_APPENDER + "Error occurred whilst trying to fetch '{}' from: {}", propertiesFileName, propertiesFileName);
            configuration = setDefaultConsumerConfigs();
        } finally {
            if (propertiesInputStream != null) {
                try {
                    propertiesInputStream.close();
                } catch (IOException e) {
                    logger.error(KafkaConsumerConstants.LOG_APPENDER + "Error occurred whilst trying to close InputStream resource used to read the '{}' file", propertiesFileName);
                }
            }
        }

        return configuration;
    }

    /**
     * Sets the default Consumer specific configurations listed in the 'KafkaConsumerConstants' class.
     *
     * @return an object of KafkaConsumerConstants class including all default Kafka Consumer specific configs.
     */
    private static KafkaConsumerConfiguration setDefaultConsumerConfigs() {
        logger.warn(KafkaConsumerConstants.LOG_APPENDER + "Default Values are being set to all Kafka Consumer specific configurations");

        KafkaConsumerConfiguration configuration = new KafkaConsumerConfiguration();

        configuration.setApplicationId(KafkaConsumerConstants.DEFAULT_CONSUMER_APPLICATION_ID);
        configuration.setBootstrap(KafkaConsumerConstants.DEFAULT_CONSUMER_BOOTSTRAP);
        configuration.setGroup(KafkaConsumerConstants.DEFAULT_CONSUMER_GROUP);
        configuration.setTopic(KafkaConsumerConstants.DEFAULT_CONSUMER_TOPIC);
        configuration.setOffsetReset(KafkaConsumerConstants.DEFAULT_CONSUMER_AUTO_OFFSET_RESET);

        return configuration;
    }
}
