package com.bigdata.engineer.fds.event.source.consumer.processor;

import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import com.bigdata.engineer.fds.event.source.consumer.config.KafkaConsumerConstants;
import com.bigdata.engineer.fds.event.source.consumer.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class StoreProcessorSupplier implements ProcessorSupplier<String, String> {
    private static final Logger logger = LogManager.getLogger(StoreProcessorSupplier.class);

    @Override
    public Processor<String, String> get() {
        return new Processor<String, String>() {
            private ProcessorContext context;
            private KeyValueStore<String , Map<String, LogEvent>> NewAccountEventStore;//Index, CustomerID, Events
            private KeyValueStore<String, Map<String, LogEvent>> DepositEventStore;
            private KeyValueStore<String, Map<String, LogEvent>> WithdrawEventStore;
            private KeyValueStore<String, Map<String, LogEvent>> TransferEventStore;
            private int newAccountIndex = 0;
            private int depositIndex = 0;
            private int withdrawIndex = 0;
            private int transferIndex = 0;

            @Override
            @SuppressWarnings("unchecked")
            public void init(ProcessorContext context) {
                // keep the processor context locally because we need it in punctuate() and commit()
                this.context = context;
                // call this processor's punctuate() method every 1000 milliseconds.
                this.context.schedule(1000);
                // retrieve the key-value store named "FraudStore"
                this.NewAccountEventStore = (KeyValueStore<String, Map<String , LogEvent>>) context.getStateStore("FraudStore.NewAccountEvent");
                this.DepositEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.DepositEvent");
                this.WithdrawEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.WithdrawEvent");
                this.TransferEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.TransferEvent");
            }

            @Override
            public void process(String dummy, String line) {
                String[] words = line.toLowerCase(Locale.getDefault()).split(" ");
                ObjectMapper mapper = new ObjectMapper();

                try {
                    for (String word : words) {
                        String index;
                        Map<String , Map<String , LogEvent>> result = new HashMap<>();
                        Map<String , LogEvent> logEventMap = new HashMap<>();
//                        logger.info(KafkaConsumerConstants.LOG_APPENDER + word);
                        if (word.contains(EventConstants.NEW_ACCOUNT_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                            NewAccountEvent newAccountEvent = mapper.readValue(word, NewAccountEvent.class);
                            logEventMap.put(newAccountEvent.getCustomerid(), newAccountEvent);
                            index = String.valueOf(newAccountIndex++);
                            result.put(index, logEventMap);
                            this.NewAccountEventStore.put(index, logEventMap);
                            nextProcess(newAccountEvent.getType(),result);
                        } else if (word.contains(EventConstants.DEPOSIT_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                            DepositEvent depositEvent = mapper.readValue(word, DepositEvent.class);
                            logEventMap.put(depositEvent.getCustomerid(), depositEvent);
                            index = String.valueOf(depositIndex++);
                            result.put(index, logEventMap);
                            this.DepositEventStore.put(index, logEventMap);
                            nextProcess(depositEvent.getType(),result);
                        } else if (word.contains(EventConstants.WITHDRAW_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                            WithdrawEvent withdrawEvent = mapper.readValue(word, WithdrawEvent.class);
                            logEventMap.put(withdrawEvent.getCustomerid(), withdrawEvent);
                            index = String.valueOf(withdrawIndex++);
                            result.put(index, logEventMap);
                            this.WithdrawEventStore.put(index, logEventMap);
                            nextProcess(withdrawEvent.getType(),result);
                        } else if (word.contains(EventConstants.TRANSFER_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                            TransferEvent transferEvent = mapper.readValue(word, TransferEvent.class);
                            logEventMap.put(transferEvent.getCustomerid(), transferEvent);
                            index = String.valueOf(transferIndex++);
                            result.put(index, logEventMap);
                            this.TransferEventStore.put(index, logEventMap);
                            nextProcess(transferEvent.getType(),result);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                context.commit();
            }

            @Override
            public void punctuate(long timestamp) {
                if (logger.isTraceEnabled()) {
                    print(this.NewAccountEventStore.name(), this.NewAccountEventStore.all(), timestamp);
                    print(this.DepositEventStore.name(), this.DepositEventStore.all(), timestamp);
                    print(this.WithdrawEventStore.name(), this.WithdrawEventStore.all(), timestamp);
                    print(this.TransferEventStore.name(), this.TransferEventStore.all(), timestamp);
                }
            }

            private void print(String name, KeyValueIterator<String, Map<String , LogEvent>> iter, long timestamp) {
                logger.trace("----------- " + name + " " + timestamp + " ----------- ");

                iter.forEachRemaining(e->{
                    if (!Objects.equals(e.value, null)) {
                        ((Map) e.value).values().forEach(f->{//Map<Integer, LogEvent>
                            ((Map) e.value).keySet().forEach(g->{
                                logger.trace(KafkaConsumerConstants.LOG_APPENDER + "[" +  g + ", " + ((LogEvent) f).getType() + ", " + e.key + ", " +  ((LogEvent) f).getTimestamp() + ", " + ((LogEvent) f).getAccountid() + ", " + ((LogEvent) f).getCreditamount() + ", " + ((LogEvent) f).getDebitamount() + ", " + ((LogEvent) f).getBeforetransferamount() + ", " + ((LogEvent) f).getReceivebankname() + ", " + ((LogEvent) f).getReceivecustomerid() + ", " + ((LogEvent) f).getTransferaccount() + ", " + ((LogEvent) f).getTransferamount() + "]");
                            });
                        });
                    }
                });
            }

            private void nextProcess(String type, Map<String , Map<String, LogEvent>> map) {
                context.forward(type, map);
                context.commit();
            }

            @Override
            public void close() {}
        };
    }
}