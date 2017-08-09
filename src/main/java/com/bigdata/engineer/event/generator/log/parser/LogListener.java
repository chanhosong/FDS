package com.bigdata.engineer.event.generator.log.parser;

import com.bigdata.engineer.event.generator.eventunit.banking.LogEvent;

public interface LogListener {
    void onDeliveryMessage(LogEvent message);
}
