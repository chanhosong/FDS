package com.bigdata.engineer.banking.system.utils;

import java.time.Instant;

public class BankingOperations {
    public static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}
