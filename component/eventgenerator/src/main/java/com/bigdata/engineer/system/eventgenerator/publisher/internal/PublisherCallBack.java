package com.bigdata.engineer.event.generator.publisher.internal;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PublisherCallBack implements Callback {
    private static final Logger logger = LogManager.getLogger(PublisherCallBack.class);

    private final long startTime;
    private final String message;
    public PublisherCallBack(long startTime, String message) {
        this.startTime = startTime;
        this.message = message;
    }
    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            if (logger.isTraceEnabled()) {
                logger.trace(
                        "message(" + message + ") sent to partition(" + metadata.partition() +
                                "), " +
                                "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            }
        } else {
            exception.printStackTrace();
        }
    }
}
