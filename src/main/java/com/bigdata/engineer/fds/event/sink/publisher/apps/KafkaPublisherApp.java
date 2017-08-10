package com.bigdata.engineer.fds.event.sink.publisher.apps;

import com.bigdata.engineer.event.generator.eventunit.banking.LogEvent;
import com.bigdata.engineer.event.generator.log.parser.LogParser;
import com.bigdata.engineer.fds.event.sink.publisher.kafka.KafkaPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class KafkaPublisherApp implements Runnable{
    private static final Logger logger = LogManager.getLogger(KafkaPublisherApp.class);

    boolean isSync = false;

    private KafkaPublisher kafkaPublisher = new KafkaPublisher("bank.events");

    @Override
    public void run () {
        kafkaPublisher.configure("localhost:9092",isSync);
        kafkaPublisher.start();

        LogParser logParser = new LogParser(new File("src/main/resources/logs/application.log"));
        logParser.addMsgListener(e->{
            try {
                try {
                    kafkaPublisher.produce(1, this.initEvent(e));
                } catch (ExecutionException | InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        });
        logParser.start();
    }

    private String initEvent(LogEvent logEvent) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(logEvent);
    }
}
