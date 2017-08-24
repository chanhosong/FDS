package com.bigdata.engineer.system.fds.processor;

import com.bigdata.engineer.system.fds.config.FDSConstants;
import com.bigdata.engineer.system.fds.config.KafkaConsumerConstants;
import com.bigdata.engineer.system.fds.domain.FraudDetectionEvent;
import com.bigdata.engineer.system.fds.domain.LogEvent;
import com.bigdata.engineer.system.fds.util.FDSOperations;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.temporal.ChronoUnit;
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
            private int fraudDetectionsIndex = 0;

            @Override
            @SuppressWarnings("unchecked")
            public void init(ProcessorContext context) {
                // keep the processor context locally because we need it in punctuate() and commit()
                this.context = context;
                // call this processor's punctuate() method every 1000 milliseconds.
                this.context.schedule(1000);
                // retrieve the key-value store named "FraudStore"
                this.NewAccountEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore(KafkaConsumerConstants.FRAUDSTORE_NEWACCOUNT);
                this.DepositEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore(KafkaConsumerConstants.FRAUDSTORE_DEPOSITEVENT);
                this.WithdrawEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore(KafkaConsumerConstants.FRAUDSTORE_WITHDRAWEVENT);
                this.TransferEventStore = (KeyValueStore<String, Map<String, LogEvent>>) context.getStateStore(KafkaConsumerConstants.FRAUDSTORE_TRANSFEREVENT);
            }

            @Override
            public void process(String type, Map<String , Map<String, LogEvent>> eventMap) {//index, customer, eventLogs
                try {
                    if (Objects.equals(type, FDSConstants.TRANSFER_EVENT_LOG_APPENDER.toLowerCase().trim())) {
                        eventMap.keySet().forEach(e->{//index로 순환
                            eventMap.values().forEach(customer-> {
                                customer.values().forEach(transferEvent->{
                                    int fraudAmount = Integer.valueOf(transferEvent.getBeforetransferamount()) - Integer.valueOf(transferEvent.getTransferamount());

                                    //1. 이체후에 10000원 이하의 수상한 잔액계좌를 확인한다
                                    if (!transferEvent.getCustomerid().matches(transferEvent.getReceivecustomerid()) && fraudAmount <= 10000) {
                                        if (logger.isDebugEnabled()) {
                                            logger.info("Step1. 비정상 감지경고(잔액이 10000원 이하):: "
                                                    + "이체한날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(transferEvent.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                    + ", 고객이름: " + transferEvent.getCustomerid()
                                                    + ", 이체 계좌: " + transferEvent.getTransferaccount()
                                                    + ", 이체 했었던 금액: "+ transferEvent.getTransferamount()
                                                    + ", 이체 직전 잔액: " + transferEvent.getBeforetransferamount()
                                                    + ", 이체후 잔액: " + fraudAmount);
                                        }

                                        //2. 신규로 계좌를 만든지 7일이하인지 확인한다
                                        this.NewAccountEventStore.all().forEachRemaining(indexStore-> {
                                            if (!Objects.equals(indexStore.value, null) && !Objects.equals(indexStore.value.get(transferEvent.getCustomerid()),null)) {
                                                LogEvent customerValue = indexStore.value.get(transferEvent.getCustomerid());

                                                if(!Instant.ofEpochMilli(Long.valueOf(transferEvent.getTimestamp())).isBefore(days7Ago)) {//계좌를 만든지 7일이 안지남 - 바정상
                                                    if(transferEvent.getTransferaccount().contains(customerValue.getAccountid())) {
                                                        if (logger.isWarnEnabled()) {
                                                            logger.info("Step2. 비정상 감지경고(잔액이 10000원이하면서 7일이내의 신규계좌):: "
                                                                    + "이체한 날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(transferEvent.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                                    + ", 계좌를 만든날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(customerValue.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                                    + ", 고객이름: " + customerValue.getCustomerid()
                                                                    + ", 계좌번호: " + customerValue.getAccountid());
                                                        }
                                                        this.DepositEventStore.all().forEachRemaining(d->{
                                                            if (!Objects.equals(d.value, null) && !Objects.equals(d.value.get(customerValue.getCustomerid()), null)) {
                                                                LogEvent depositEvent = d.value.get(customerValue.getCustomerid());
                                                                if ( 900000 <= Integer.valueOf(depositEvent.getCreditamount()) && Integer.valueOf(depositEvent.getCreditamount()) <= 1000000) {
                                                                    if (logger.isWarnEnabled()) {
                                                                        logger.warn("Step3. 비정상 감지경고 입금내역(7일이내 입금한 금액이 90-100만원이상):: "
                                                                                + "이체한 날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(transferEvent.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                                                + ", 입금날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(depositEvent.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                                                + ", 고객이름: " + depositEvent.getCustomerid()
                                                                                + ", 계좌번호: " + depositEvent.getAccountid()
                                                                                + ", 입금액: " + depositEvent.getCreditamount());
                                                                    }
                                                                    this.WithdrawEventStore.all().forEachRemaining(w->{
                                                                        if (!Objects.equals(w.value, null) && !Objects.equals(w.value.get(customerValue.getCustomerid()), null)) {
                                                                            LogEvent withdrawEvent = w.value.get(customerValue.getCustomerid());
                                                                            Instant depositTimestamp = Instant.ofEpochMilli(Long.valueOf(depositEvent.getTimestamp()));
                                                                            Instant withdrawTimestamp = Instant.ofEpochMilli(Long.valueOf(withdrawEvent.getTimestamp()));

                                                                            if(ChronoUnit.SECONDS.between(depositTimestamp, withdrawTimestamp) <= 7200) {//2시간 이내에 출금된 기록을 확인함
                                                                                if (logger.isDebugEnabled()) {
                                                                                    logger.warn("Step4. 비정상 감지경고(출금시간이 2시간이내):: "
                                                                                            + "출금한 날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(withdrawEvent.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                                                            + ", 입금한날짜: " + formatter.format(Instant.ofEpochMilli(Long.valueOf(depositEvent.getTimestamp())).atZone(ZoneId.systemDefault()))
                                                                                            + ", 고객이름: " + withdrawEvent.getCustomerid()
                                                                                            + ", 계좌번호: " + withdrawEvent.getAccountid()
                                                                                            + ", 출금액: " + withdrawEvent.getDebitamount());
                                                                                }

                                                                                FraudDetectionEvent fraudDetectionEvent = new FraudDetectionEvent();
                                                                                fraudDetectionEvent.setType(FDSConstants.FRAUD_DETECTION_EVENT_LOG_APPENDER);
                                                                                fraudDetectionEvent.setTimestamp(
                                                                                        FDSOperations.getTimestamp());
                                                                                fraudDetectionEvent.setCustomerid(withdrawEvent.getCustomerid());
                                                                                fraudDetectionEvent.setAccountid(customerValue.getAccountid());
                                                                                fraudDetectionEvent.setTransferaccount(transferEvent.getTransferaccount());
                                                                                fraudDetectionEvent.setBeforetransferamount(transferEvent.getBeforetransferamount());
                                                                                fraudDetectionEvent.setReceivebankname(transferEvent.getReceivebankname());
                                                                                fraudDetectionEvent.setReceivecustomerid(transferEvent.getReceivecustomerid());
                                                                                fraudDetectionEvent.setCreditamount(depositEvent.getCreditamount());
                                                                                fraudDetectionEvent.setDebitamount(withdrawEvent.getDebitamount());
                                                                                fraudDetectionEvent.setTransferamount(transferEvent.getTransferamount());

                                                                                String fraudDetectionEventJSON = null;
                                                                                try {
                                                                                    fraudDetectionEventJSON = mapper.writeValueAsString(fraudDetectionEvent);
                                                                                } catch (JsonProcessingException e1) {
                                                                                    e1.printStackTrace();
                                                                                }
//
                                                                                logger.error("Step5. 최종 비정상 이체내역::  " + fraudDetectionEventJSON);

                                                                                nextProcess(String.valueOf(fraudDetectionsIndex++), fraudDetectionEventJSON);
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });


                                        /*next process*/
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

            private void nextProcess(String index, String json) {
                context.forward(index, json);
                context.commit();
            }

            @Override
            public void close() {}
        };
    }
}