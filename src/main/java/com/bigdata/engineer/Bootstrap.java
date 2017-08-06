package com.bigdata.engineer;

import com.bigdata.engineer.event.generator.EventGenerator;

public class Bootstrap {
    public static void main(String[] args) {
	    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
	    System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "info");
	    System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
	    System.setProperty("org.apache.commons.logging.simplelog.dateTimeFormat", "HH:mm:ss");
		try {
//			KafkaConsumer.init();
			new EventGenerator().init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
