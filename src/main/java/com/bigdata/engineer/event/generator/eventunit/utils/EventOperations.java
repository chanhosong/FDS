package com.bigdata.engineer.event.generator.eventunit.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    public static String randomIdentifier() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<>();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
}
