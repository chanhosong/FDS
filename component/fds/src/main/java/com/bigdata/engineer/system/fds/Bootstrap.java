package com.bigdata.engineer.system.fds;

import com.bigdata.engineer.system.eventgenerator.actor.EventGeneratorImpl;
import com.bigdata.engineer.system.fds.actor.FraudDetectionProcessorImpl;

public class Bootstrap {
    public static void main(String[] args) {
		try {
			new EventGeneratorImpl().run();//1.create log and publishing event to topic 'bank.events'
            new FraudDetectionProcessorImpl().run();//2.consuming kafka stream event

//			new Thread(new EventGenerator()).start();
//            new Thread(new FraudDetectionProcessor()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}