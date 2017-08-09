//package com.bigdata.engineer.fds.event.sink.publisher.factory;//package com.bigdata.engineer.fds.event.test.factory;
//
//import com.bigdata.engineer.fds.event.sink.publisher.internal.MessageSerializerType;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//
//import java.util.Properties;
//
///**
// * This factory class creates different types of kafka producer instances, based on given message type.
// *
// * @see MessageSerializerType
// */
//public class KafkaProducerFactory {
//
//    /**
//     * Creates kafka producers based on given message serializer type.
//     *
//     * @param serializerType    message serializer type
//     * @param properties        properties to be used to send a message
//     * @return                  created kafka producer
//     */
//    public Producer createProducer(MessageSerializerType serializerType, Properties properties) {
//        Producer producer;
//
//        switch (serializerType) {
//                case BYTE_SERIALIZER:
//                    producer = new Producer<String, byte[]>(new ProducerConfig(properties));
//                    break;
//                case STRING_SERIALIZER:
//                    producer = new Producer<String, String>(new ProducerConfig(properties));
//                    break;
//                default:
//                    throw new IllegalArgumentException("Incorrect serialazier class specified...");
//        }
//        return producer;
//    }
//}