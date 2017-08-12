package com.bigdata.engineer;

import com.bigdata.engineer.event.generator.EventGenerator;
import com.bigdata.engineer.fds.event.source.consumer.FraudDetectionProcessor;

public class Bootstrap {
    public static void main(String[] args) {
	    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
	    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "info");
	    System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
	    System.setProperty("org.apache.commons.logging.simplelog.dateTimeFormat", "HH:mm:ss");
		try {
			new EventGenerator().run();//1.create log and publishing event to topic 'bank.events'
            new FraudDetectionProcessor().run();//2.consuming kafka stream event

//			new Thread(new EventGenerator()).start();
//            new Thread(new FraudDetectionProcessor()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
