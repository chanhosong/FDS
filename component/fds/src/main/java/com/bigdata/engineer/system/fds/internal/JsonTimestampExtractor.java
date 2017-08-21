package com.bigdata.engineer.system.fds.internal;

import com.bigdata.engineer.system.fds.domain.DepositEvent;
import com.bigdata.engineer.system.fds.domain.NewAccountEvent;
import com.bigdata.engineer.system.fds.domain.TransferEvent;
import com.bigdata.engineer.system.fds.domain.WithdrawEvent;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

/**
 * A timestamp extractor implementation that tries to extract event time from
 * the "timestamp" field in the Json formatted message.
 */
public class JsonTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(final ConsumerRecord<Object, Object> record, final long previousTimestamp) {
        if (record.value() instanceof NewAccountEvent) {
            return Long.valueOf(((NewAccountEvent) record.value()).getTimestamp());
        }

        if (record.value() instanceof DepositEvent) {
            return Long.valueOf(((DepositEvent) record.value()).getTimestamp());
        }

        if (record.value() instanceof WithdrawEvent) {
            return Long.valueOf(((WithdrawEvent) record.value()).getTimestamp());
        }

        if (record.value() instanceof TransferEvent) {
            return Long.valueOf(((TransferEvent) record.value()).getTimestamp());
        }

        if (record.value() instanceof JsonNode) {
            return ((JsonNode) record.value()).get("timestamp").longValue();
        }

        throw new IllegalArgumentException("JsonTimestampExtractor cannot recognize the record value " + record.value());
    }
}