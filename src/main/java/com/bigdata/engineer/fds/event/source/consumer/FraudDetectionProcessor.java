package com.bigdata.engineer.fds.event.source.consumer;

import com.bigdata.engineer.fds.event.source.consumer.config.KafkaConfigOperations;
import com.bigdata.engineer.fds.event.source.consumer.config.KafkaConsumerConstants;
import com.bigdata.engineer.fds.event.source.consumer.internal.BankingEventSerde;
import com.bigdata.engineer.fds.event.source.consumer.processor.RuleEngine;
import com.bigdata.engineer.fds.event.source.consumer.processor.StoreProcessorSupplier;
import org.apache.kafka.common.utils.Exit;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class FraudDetectionProcessor {
    private static final Logger logger = LogManager.getLogger(FraudDetectionProcessor.class);

    public void init() throws Exception {
        logger.info("Start Fraud Detection System! Current date and time {}.", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        TopologyBuilder builder = new TopologyBuilder();

        builder.addSource(KafkaConsumerConstants.KAFKASOURCE, "bank.events");

        builder.addProcessor(KafkaConsumerConstants.STOREPROCESS, new StoreProcessorSupplier(), KafkaConsumerConstants.KAFKASOURCE);
        builder.addProcessor(KafkaConsumerConstants.RULEPROCESS, new RuleEngine(), KafkaConsumerConstants.KAFKASOURCE);
        builder.addStateStore(Stores.create("FraudStore.NewAccountEvent").withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create("FraudStore.DepositEvent").withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create("FraudStore.WithdrawEvent").withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create("FraudStore.TransferEvent").withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create("FraudStore.FraudDetections").withIntegerKeys().withStringValues().inMemory().build(), KafkaConsumerConstants.RULEPROCESS);

        builder.addSink(KafkaConsumerConstants.KAFKASINK, "fds.detections", KafkaConsumerConstants.RULEPROCESS);

        final KafkaStreams streams = new KafkaStreams(builder, KafkaConfigOperations.consumerProps());
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
            latch.countDown();
        }));

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            Exit.exit(1);
        }
        Exit.exit(0);
    }
}
