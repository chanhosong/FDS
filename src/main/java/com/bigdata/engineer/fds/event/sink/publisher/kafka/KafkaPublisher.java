package com.bigdata.engineer.fds.event.sink.publisher.kafka;

import com.bigdata.engineer.fds.event.sink.publisher.internal.PublisherCallBack;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaPublisher implements KafkaProducerHandler {
    private static final Logger logger = LogManager.getLogger(KafkaPublisher.class);

    String topic;
    boolean isSync;
    private Properties kafkaProps = new Properties();
    private Producer<Integer, String> producer;

    public KafkaPublisher(String topic) {
        this.topic = topic;
    }

    @Override
    public void configure(String brokerList, boolean isSync) {
        this.isSync = isSync;
        kafkaProps.put("bootstrap.servers", brokerList);

        // This is mandatory, even though we don't send keys
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("acks", "1");

        // how many times to retry when produce request fails?
        kafkaProps.put("retries", "3");
        kafkaProps.put("linger.ms", 5);
    }

    @Override
    public void start() {
        producer = new KafkaProducer<>(kafkaProps);
    }

    @Override
    public void produce(int messageNo, String value) throws ExecutionException, InterruptedException {
        if (isSync)
            produceSync(value);
        else if (!isSync)
            produceAsync(messageNo, value);
        else throw new IllegalArgumentException("Expected sync or async, got " + isSync);

    }

    @Override
    public void close() {
        producer.close();
    }

    /* Produce a record and wait for server to reply. Throw an exception if something goes wrong */
    private void produceSync(String value) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        producer.send(new ProducerRecord<>(topic, value)).get();
        long elapsedTime = System.currentTimeMillis() - startTime;

    }

    /* Produce a record without waiting for server. This includes a callback that will print an error if something goes wrong */
    private void produceAsync(int messageNo, String value) {
        long startTime = System.currentTimeMillis();
        producer.send(new ProducerRecord<>(topic, messageNo, value), new PublisherCallBack(startTime, messageNo, value));
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     */
//    private class DemoProducerCallback implements Callback {
//        @Override
//        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//            if (e != null) {
//                System.out.println("Error producing to topic " + recordMetadata.topic());
//                e.printStackTrace();
//            }
//        }
//    }
}