package com.bigdata.engineer.system.fds.util;

import java.time.Instant;

public class FDSOperations {
    public static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}
