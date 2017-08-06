package com.bigdata.engineer.fds.event.sink.publisher.internal;

/**
 * Represents possible message types that can be sent to kafka. 
 */ 
public enum MessageSerializerType { 
 
    STRING_SERIALIZER ("kafka.serializer.StringEncoder"), 
    BYTE_SERIALIZER ("kafka.serializer.DefaultEncoder"); 
 
 
    private String value; 
 
    MessageSerializerType(String value) { 
        this.value = value; 
    } 
 
    public String getValue() { 
        return value; 
    } 
 
    @Override 
    public String toString() { 
        return this.getValue(); 
    } 
 
    public static MessageSerializerType getEnum(String value) {
        if(value == null) throw new IllegalArgumentException(); 
 
        for(MessageSerializerType values : values()) {
            if(value.equalsIgnoreCase(values.getValue())) 
                return values; 
        } 
 
        throw new IllegalArgumentException(); 
    } 
}
