package com.bigdata.engineer.fds.event.source.consumer.internal;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class MessageSerializerType {
    public Deserializer stringKeyDeserializer() {
        return new StringDeserializer();
    }

    public Deserializer payloadStringValueDeserializer() {
        return new StringDeserializer();
    }

    public Deserializer payloadJsonValueDeserializer() {
//        return new JsonDeserializer(EventUnit.class);
        return null;
    }
}
