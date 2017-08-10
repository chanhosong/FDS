package com.bigdata.engineer.fds.event.source.consumer.processor;

import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import com.bigdata.engineer.fds.event.source.consumer.config.KafkaConsumerConstants;
import com.bigdata.engineer.fds.event.source.consumer.domain.NewAccountEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;

public class MyProcessorSupplier implements ProcessorSupplier<String, String> {
    private static final Logger logger = LogManager.getLogger(MyProcessorSupplier.class);

    @Override
    public Processor<String, String> get() {
        return new Processor<String, String>() {
            private ProcessorContext context;
            private KeyValueStore<String, Integer> kvStore;

            @Override
            @SuppressWarnings("unchecked")
            public void init(ProcessorContext context) {
                // keep the processor context locally because we need it in punctuate() and commit()
                this.context = context;
                // call this processor's punctuate() method every 1000 milliseconds.
                this.context.schedule(1000);
                // retrieve the key-value store named "Counts"
                this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore("FraudStore");
            }

            @Override
            public void process(String dummy, String line) {
                String[] words = line.toLowerCase(Locale.getDefault()).split(" ");
                ObjectMapper mapper = new ObjectMapper();

                try {
                    for (String word : words) {
                        logger.info(KafkaConsumerConstants.LOG_APPENDER + word);
                        logger.info(KafkaConsumerConstants.LOG_APPENDER + EventConstants.NEW_ACCOUNT_EVENT_LOG_APPENDER.toLowerCase().trim());
                        logger.info(KafkaConsumerConstants.LOG_APPENDER + word.contains(EventConstants.NEW_ACCOUNT_EVENT_LOG_APPENDER.toLowerCase().trim()));
                        if (word.contains(EventConstants.NEW_ACCOUNT_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                            NewAccountEvent newAccountEvent = mapper.readValue(word, NewAccountEvent.class);
                            System.out.println(newAccountEvent.getCustomerid());
                            Integer oldValue = this.kvStore.get(newAccountEvent.getCustomerid());
//                            PropertyUtils.getProperty();
//
//                            select distinct(client) from open_positions where exchange = 'NASDAQ'


                            if (oldValue == null) {
                                this.kvStore.put(newAccountEvent.getCustomerid(), 1);
                            } else {
                                this.kvStore.put(newAccountEvent.getCustomerid(), oldValue + 1);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // commit the current processing progress
                context.commit();
            }

            @Override
            public void punctuate(long timestamp) {
                try (KeyValueIterator<String, Integer> iter = this.kvStore.all()) {
                    System.out.println("----------- " + timestamp + " ----------- ");

                    while (iter.hasNext()) {
                        KeyValue<String, Integer> entry = iter.next();

                        System.out.println("[" + (entry.key) + ", " + entry.value + "]");

                        context.forward(entry.key, entry.value.toString());
                    }
                }
            }

            @Override
            public void close() {}
        };
    }
}