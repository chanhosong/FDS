package com.bigdata.engineer.fds.event.sink.publisher.kafka;

import com.bigdata.engineer.fds.event.sink.publisher.internal.PublisherCallBack;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Publisher implements Runnable {
    private static final Logger logger = LogManager.getLogger(Publisher.class);

    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isAsync;
    public Publisher(String topic, Boolean isAsync) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("client.id", "Producer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
//        producer = new KafkaProducerHandler<>(KafkaConfigOperations.publisherProps());
        this.topic = topic;
        this.isAsync = isAsync;
    }

    public void run() {
        int messageNo = 1;
        while (true) {
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if (isAsync) { // Send asynchronously
                producer.send(new ProducerRecord<>(topic, messageNo, messageStr), new PublisherCallBack(startTime, messageNo, messageStr));
            } else { // Send synchronously
                try {
                    producer.send(new ProducerRecord<>(topic, messageNo, messageStr)).get();
                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            ++messageNo;
        }
    }
}

//class PublisherCallBack implements Callback {
//    private final long startTime;
//    private final int key;
//    private final String message;
//    public PublisherCallBack(long startTime, int key, String message) {
//        this.startTime = startTime;
//        this.key = key;
//        this.message = message;
//    }
//    /**
//     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
//     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
//     * non-null.
//     *
//     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
//     *                  occurred.
//     * @param exception The exception thrown during processing of this record. Null if no error occurred.
//     */
//    public void onCompletion(RecordMetadata metadata, Exception exception) {
//        long elapsedTime = System.currentTimeMillis() - startTime;
//        if (metadata != null) {
//            System.out.println(
//                    "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
//                            "), " +
//                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
//        } else {
//            exception.printStackTrace();
//        }
//    }
//}
