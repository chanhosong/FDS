package com.bigdata.engineer.fds.event.source.consumer.processor;

import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import com.bigdata.engineer.fds.event.source.consumer.config.KafkaConsumerConstants;
import com.bigdata.engineer.fds.event.source.consumer.domain.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class RuleEngine implements ProcessorSupplier<String, Map<String , Map<String, LogEvent>>> {
    private static final Logger logger = LogManager.getLogger(RuleEngine.class);

    @Override
    public Processor<String, Map<String , Map<String, LogEvent>>> get() {
        return new Processor<String, Map<String , Map<String, LogEvent>>>() {
            private ProcessorContext context;
            private KeyValueStore<String, Map<String, LogEvent>> NewAccountEventStore;//Index, CustomerID, Events
            private KeyValueStore<String, Map<String, LogEvent>> DepositEventStore;
            private KeyValueStore<String, Map<String, LogEvent>> WithdrawEventStore;
            private KeyValueStore<String, Map<String, LogEvent>> TransferEventStore;
            private DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            private Instant days7Ago = ZonedDateTime.now().minusDays(7).toInstant();
            private ObjectMapper mapper = new ObjectMapper();
//            private KeyValueStore<String, String> FraudDetections;
            private int fraudDetectionsIndex = 0;

            @Override
            @SuppressWarnings("unchecked")
            public void init(ProcessorContext context) {
                // keep the processor context locally because we need it in punctuate() and commit()
                this.context = context;
                // call this processor's punctuate() method every 1000 milliseconds.
                this.context.schedule(1000);
                // retrieve the key-value store named "FraudStore"
                this.NewAccountEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.NewAccountEvent");
                this.DepositEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.DepositEvent");
                this.WithdrawEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.WithdrawEvent");
                this.TransferEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore("FraudStore.TransferEvent");
//                this.FraudDetections = (KeyValueStore<String, String>) context.getStateStore("FraudStore.FraudDetections");
            }

            @Override
            public void process(String type, Map<String , Map<String, LogEvent>> eventMap) {
                try {
//                    System.out.println(eventMap.keySet() + " " + eventMap.values());
                    if (Objects.equals(type, EventConstants.TRANSFER_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                        eventMap.keySet().forEach(e->{//index로 순환
                            eventMap.values().forEach(customer-> {
                                customer.values().forEach(customervalues->{
                                    int fraudAmount = Integer.valueOf(customervalues.getBeforetransferamount()) - Integer.valueOf(customervalues.getTransferamount());

                                    //1. 이체후에 10000원 이하의 수상한 잔액계좌를 확인한다
                                    if (fraudAmount <= 10000) {
                                        if (logger.isDebugEnabled()) {
                                            logger.debug(customervalues.getTimestamp() + " " + customervalues.getCustomerid() + " " + customervalues.getTransferaccount() + " " + customervalues.getBeforetransferamount() + " " + fraudAmount);
                                        }

                                        //2. 신규로 계좌를 만든지 7일이하인지 확인한다
                                        this.NewAccountEventStore.all().forEachRemaining(indexStore-> {
                                            if (!Objects.equals(indexStore.value, null) && !Objects.equals(indexStore.value.get(customervalues.getCustomerid()),null)) {
                                                LogEvent customerValue = indexStore.value.get(customervalues.getCustomerid());
                                                if(Instant.ofEpochMilli(Long.valueOf(customervalues.getTimestamp())).isAfter(days7Ago)) {
                                                    System.out.println("이체한 날짜: " + formatter.format(Instant.ofEpochMilli(
                                                            Long.valueOf(customervalues.getTimestamp())).atZone(ZoneId.systemDefault())) + ", 계좌를 만든날짜: " + formatter.format(
                                                            Instant.ofEpochMilli(Long.valueOf(customerValue.getTimestamp())).atZone(
                                                                    ZoneId.systemDefault())) + " " + customerValue.getCustomerid() + " " + customerValue.getAccountid() + ", 현재 잔액" + fraudAmount);
                                                } else {
                                                    System.out.println("7일이상이 있는지 테스트: "+ formatter.format(Instant.ofEpochMilli(Long.valueOf(customerValue.getTimestamp())).atZone(ZoneId.systemDefault())));
                                                }
                                            }
                                        });

                                        /*next process??*/
//                                        this.NewAccountEventStore.all().forEachRemaining(a->{
//                                            if (!Objects.equals(a.value, null)) {
//                                                ((Map) a.value).values().forEach(f->{//Map<Integer, LogEvent>
//                                                    ((Map) a.value).keySet().forEach(g->{
//                                                        if (((LogEvent) f).getTimestamp() == customervalues.getTimestamp())//2. 신규로 계좌를 만든지 7일
//                                                        System.out.println(KafkaConsumerConstants.LOG_APPENDER + "[" + g + ", " + ((LogEvent) f).getType() + ", " + a.key + ", " +  ((LogEvent) f).getTimestamp() + ", " + ((LogEvent) f).getAccountid() + ", " + ((LogEvent) f).getCreditamount() + ", " + ((LogEvent) f).getDebitamount() + ", " + ((LogEvent) f).getBeforetransferamount() + ", " + ((LogEvent) f).getReceivebankname() + ", " + ((LogEvent) f).getReceivecustomerid() + ", " + ((LogEvent) f).getTransferaccount() + ", " + ((LogEvent) f).getTransferamount() + "]");
//                                                    });
//                                                });
//                                            }
//                                        });
//                                        nextProcess(customervalues.getCustomerid(), customervalues.getTransferaccount());
                                    }
                                });
                            });
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // commit the current processing progress
                context.commit();
            }

            @Override
            public void punctuate(long timestamp) {
                if (logger.isDebugEnabled()) {
                    print(this.NewAccountEventStore.name(), this.NewAccountEventStore.all(), timestamp);
                    print(this.DepositEventStore.name(), this.DepositEventStore.all(), timestamp);
                    print(this.WithdrawEventStore.name(), this.WithdrawEventStore.all(), timestamp);
                    print(this.TransferEventStore.name(), this.TransferEventStore.all(), timestamp);
                }
            }

            private void print(String name, KeyValueIterator<String, Map<String, LogEvent>> iter, long timestamp) {
                logger.debug("----------- " + name + " " + timestamp + " ----------- ");

                iter.forEachRemaining(e->{
                    if (!Objects.equals(e.value, null)) {
                        ((Map) e.value).values().forEach(f->{//Map<Integer, LogEvent>
                            ((Map) e.value).keySet().forEach(g->{
                               logger.debug(KafkaConsumerConstants.LOG_APPENDER + "[" + g + ", " + ((LogEvent) f).getType() + ", " + e.key + ", " +  ((LogEvent) f).getTimestamp() + ", " + ((LogEvent) f).getAccountid() + ", " + ((LogEvent) f).getCreditamount() + ", " + ((LogEvent) f).getDebitamount() + ", " + ((LogEvent) f).getBeforetransferamount() + ", " + ((LogEvent) f).getReceivebankname() + ", " + ((LogEvent) f).getReceivecustomerid() + ", " + ((LogEvent) f).getTransferaccount() + ", " + ((LogEvent) f).getTransferamount() + "]");
                            });
                        });
                    }
                });
            }

            private void nextProcess(String type, String json) {
                context.forward(type, json);
                context.commit();
            }

            @Override
            public void close() {}
        };
    }
}