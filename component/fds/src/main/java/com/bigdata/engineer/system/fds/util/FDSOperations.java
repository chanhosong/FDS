package com.bigdata.engineer.fds.consumer.util;

import java.time.Instant;

public class FDSOperations {
    public static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}
