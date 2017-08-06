package com.bigdata.engineer.event.generator.eventunit.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

public class EventOperations {
    public static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
    public static int getRandom(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
    public static String getCustomerIDGenerator(int numBits, int radix) {
        return new BigInteger(numBits, new SecureRandom()).toString(radix);//RandomStringUtils.random(8, true, true)
    }
}
