package com.bigdata.engineer.system.fds.actor;

import com.bigdata.engineer.system.fds.config.KafkaConfigOperations;
import com.bigdata.engineer.system.fds.config.KafkaConsumerConstants;
import com.bigdata.engineer.system.fds.internal.BankingEventSerde;
import com.bigdata.engineer.system.fds.processor.RuleEngine;
import com.bigdata.engineer.system.fds.processor.StoreProcessorSupplier;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.kafka.common.utils.Exit;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author ChanHo Song
 */
@Component(name="fds-service")
@Service
public class FraudDetectionProcessorImpl implements FraudDetectionProcessor {
    private static final Logger logger = LogManager.getLogger(FraudDetectionProcessorImpl.class);

    @Override
    public void run() {
        logger.info("Start Fraud Detection System! Current date and time {}.", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        TopologyBuilder builder = new TopologyBuilder();

        builder.addSource(KafkaConsumerConstants.KAFKASOURCE, "bank.events");

        builder.addProcessor(KafkaConsumerConstants.STOREPROCESS, new StoreProcessorSupplier(), KafkaConsumerConstants.KAFKASOURCE);
        builder.addProcessor(KafkaConsumerConstants.RULEPROCESS, new RuleEngine(), KafkaConsumerConstants.STOREPROCESS);

        builder.addStateStore(Stores.create(KafkaConsumerConstants.FRAUDSTORE_NEWACCOUNT).withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create(KafkaConsumerConstants.FRAUDSTORE_DEPOSITEVENT).withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create(KafkaConsumerConstants.FRAUDSTORE_WITHDRAWEVENT).withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create(KafkaConsumerConstants.FRAUDSTORE_TRANSFEREVENT).withStringKeys().withValues(new BankingEventSerde().logEventSerde()).inMemory().build(), KafkaConsumerConstants.STOREPROCESS, KafkaConsumerConstants.RULEPROCESS);
        builder.addStateStore(Stores.create(KafkaConsumerConstants.FRAUDSTORE_FRAUDDETECTIONS).withStringKeys().withStringValues().inMemory().build(), KafkaConsumerConstants.RULEPROCESS);

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
