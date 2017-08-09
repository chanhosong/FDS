package com.bigdata.engineer.fds.event.sink.publisher.apps;

import com.bigdata.engineer.event.generator.eventunit.banking.LogEvent;
import com.bigdata.engineer.event.generator.log.parser.LogParser;
import com.bigdata.engineer.fds.event.sink.publisher.kafka.KafkaPublisher;
import com.bigdata.engineer.fds.event.sink.publisher.kafka.config.KafkaPublisherConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class KafkaPublisherApp {
    private static final Logger logger = LogManager.getLogger(KafkaPublisherApp.class);

    boolean isSync = true;

    private KafkaPublisher kafkaPublisher = new KafkaPublisher(KafkaPublisherConstants.PUBLISHER_TOPIC);

    public void run () {
        kafkaPublisher.configure("localhost:9092",isSync);
        kafkaPublisher.start();

        LogParser logParser = new LogParser(new File("src/main/resources/logs/application.log"));
        logParser.addMsgListener(e->{
            try {
                System.out.println(this.initEvent(e));
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        });
        logParser.init();

//        kafkaPublisher.produce(1, "JSON");
//        kafkaPublisher.close();
    }

    private String initEvent(LogEvent logEvent) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(logEvent);
    }
}
