package com.bigdata.engineer.fds.event.sink.publisher.kafka.config;

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

    public static Properties publisherProps() {
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, readKafkaConfigs().getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, readKafkaConfigs().getBootstrap());
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    private static KafkaPublisherConfiguration readKafkaConfigs() {
        String propertiesFileName = KafkaPublisherConstants.KAFKA_PROPERTIES_FILE_NAME;
        KafkaPublisherConfiguration configuration = new KafkaPublisherConfiguration();
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
                    logger.debug(KafkaPublisherConstants.LOG_APPENDER + "Property path: {}", root + propertiesFileName);
                }
            } else {
                propertiesInputStream = new FileInputStream(root);
                logger.info(KafkaPublisherConstants.LOG_APPENDER + "Property path: {}", root);
            }

            //load a properties file from class path, inside static method
            properties.load(propertiesInputStream);

            //init Publisher Configuration
            configuration.setApplicationId(properties.getProperty(KafkaPublisherConstants.PUBLISHER_APPLICATION_ID));
            configuration.setBootstrap(properties.getProperty(KafkaPublisherConstants.PUBLISHER_BOOTSTRAP));
            configuration.setTopic(properties.getProperty(KafkaPublisherConstants.PUBLISHER_TOPIC));

            logger.info(KafkaPublisherConstants.LOG_APPENDER + "Application ID: {}", configuration.getApplicationId());
            logger.info(KafkaPublisherConstants.LOG_APPENDER + "Bootstrap: {}", configuration.getBootstrap());
            logger.info(KafkaPublisherConstants.LOG_APPENDER + "Topic: {}", configuration.getTopic());

        } catch (FileNotFoundException ex) {
            logger.error(KafkaPublisherConstants.LOG_APPENDER + "Unable to find {} file at: {}", propertiesFileName, propertiesFileName);
            configuration = setDefaultPublisherConfigs();
        } catch (IOException ex) {
            logger.error(KafkaPublisherConstants.LOG_APPENDER + "Error occurred whilst trying to fetch '{}' from: {}", propertiesFileName, propertiesFileName);
            configuration = setDefaultPublisherConfigs();
        } finally {
            if (propertiesInputStream != null) {
                try {
                    propertiesInputStream.close();
                } catch (IOException e) {
                    logger.error(KafkaPublisherConstants.LOG_APPENDER + "Error occurred whilst trying to close InputStream resource used to read the '{}' file",propertiesFileName);
                }
            }
        }

        return configuration;
    }

    /**
     * Sets the default Publisher specific configurations listed in the 'KafkaPublisherConstants' class.
     *
     * @return an object of KafkaPublisherConstants class including all default Kafka Publisher specific configs.
     */
    private static KafkaPublisherConfiguration setDefaultPublisherConfigs() {
        logger.warn(KafkaPublisherConstants.LOG_APPENDER + "Default Values are being set to all Kafka Publisher specific configurations");

        KafkaPublisherConfiguration configuration = new KafkaPublisherConfiguration();

        configuration.setApplicationId(KafkaPublisherConstants.DEFAULT_PUBLISHER_APPLICATION_ID);
        configuration.setBootstrap(KafkaPublisherConstants.DEFAULT_PUBLISHER_BOOTSTRAP);
        configuration.setTopic(KafkaPublisherConstants.DEFAULT_PUBLISHER_TOPIC);

        return configuration;
    }
}
