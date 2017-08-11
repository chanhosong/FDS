package com.bigdata.engineer.fds.event.source.consumer.internal;

import com.bigdata.engineer.fds.event.source.consumer.domain.*;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BankingEventSerde {
    private static final Logger logger = LogManager.getLogger(BankingEventSerde.class);

    private Map<String, Object> serdeProps = new HashMap<>();

    public Serde<LogEvent> logEventSerde() {
        final Serializer<LogEvent> logEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", LogEvent.class);
        logEventSerializer.configure(serdeProps, false);

        final Deserializer<LogEvent> logEventDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", NewAccountEvent.class);
        logEventDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(logEventSerializer, logEventDeserializer);
    }

    public Serde<NewAccountEvent> newAccountEventSerde() {
        final Serializer<NewAccountEvent> newAccountEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", NewAccountEvent.class);
        newAccountEventSerializer.configure(serdeProps, false);

        final Deserializer<NewAccountEvent> newAccountEventDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", NewAccountEvent.class);
        newAccountEventDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(newAccountEventSerializer, newAccountEventDeserializer);
    }

    public Serde<DepositEvent> depositEventSerde() {
        final Serializer<DepositEvent> depositEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", DepositEvent.class);
        depositEventSerializer.configure(serdeProps, false);

        final Deserializer<DepositEvent> depositEventDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", DepositEvent.class);
        depositEventDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(depositEventSerializer, depositEventDeserializer);
    }

    public Serde<WithdrawEvent> withdrawEventSerde() {
        final Serializer<WithdrawEvent> withdrawEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", WithdrawEvent.class);
        withdrawEventSerializer.configure(serdeProps, false);

        final Deserializer<WithdrawEvent> withdrawEventDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", WithdrawEvent.class);
        withdrawEventDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(withdrawEventSerializer, withdrawEventDeserializer);
    }

    public Serde<TransferEvent> transferEventSerde() {
        final Serializer<TransferEvent> transferEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", TransferEvent.class);
        transferEventSerializer.configure(serdeProps, false);

        final Deserializer<TransferEvent> transferEventDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", TransferEvent.class);
        transferEventDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(transferEventSerializer, transferEventDeserializer);
    }
}
