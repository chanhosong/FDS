package com.bigdata.engineer.banking.system.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;

public class AccountOperations {
    public static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
//    public static String getAccountIDGenerator(int seed, int randomNumberOrigin, int randomnumberBound) {
//        return new Random(seed).ints(randomNumberOrigin, randomnumberBound).findFirst().toString() + "-" + new Random(seed+10).ints(randomNumberOrigin, randomnumberBound).findFirst().toString();
//    }
    public static String getAccountIDGenerator(int numBits, int radix) {
        return new BigInteger(numBits, new SecureRandom()).toString(radix) + "-" + new BigInteger(numBits, new SecureRandom()).toString(radix);//RandomStringUtils.random(8, true, true)
    }
}
