package com.bigdata.engineer.system.eventgenerator.log.parser;

import com.bigdata.engineer.system.eventgenerator.eventunit.banking.LogEvent;

public interface LogListener {
    void onDeliveryMessage(LogEvent message);
}
