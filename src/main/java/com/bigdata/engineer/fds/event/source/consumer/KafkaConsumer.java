package com.bigdata.engineer.fds.event.source.consumer;

import com.bigdata.engineer.fds.event.source.consumer.config.KafkaConfigOperations;
import com.bigdata.engineer.fds.event.source.consumer.processor.MyProcessorSupplier;
import org.apache.kafka.common.utils.Exit;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class KafkaConsumer {
    private static final Logger logger = LogManager.getLogger(KafkaConsumer.class);

    public static void init() throws Exception {
        logger.info("Start Fraud Detection System! Current date and time {}.", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if(logger.isDebugEnabled()){
            logger.debug("This is debug");
        }

        TopologyBuilder builder = new TopologyBuilder();

        builder.addSource("Source", "bank.events.test");

        builder.addProcessor("Process", new MyProcessorSupplier(), "Source");
        builder.addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().inMemory().build(),
                "Process");

        builder.addSink("Sink", "streams-wordcount-processor-output", "Process");

        final KafkaStreams streams = new KafkaStreams(builder, KafkaConfigOperations.consumerProps());
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("fds.detections.test") {
            @Override
            public void run() {
                System.out.println("test");
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            Exit.exit(1);
        }
        Exit.exit(0);
    }
}
