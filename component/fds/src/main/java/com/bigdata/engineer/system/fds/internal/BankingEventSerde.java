package com.bigdata.engineer.system.fds.internal;

import com.bigdata.engineer.system.fds.domain.LogEvent;
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
        serdeProps.put("JsonPOJOClass", LogEvent.class);
        logEventDeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(logEventSerializer, logEventDeserializer);
    }
}
