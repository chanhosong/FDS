package com.bigdata.engineer.system.eventgenerator;

import com.bigdata.engineer.system.eventgenerator.actor.EventGenerator;
import com.bigdata.engineer.system.eventgenerator.actor.EventGeneratorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.*;

public class Activator implements BundleActivator, ServiceListener {
    private static final Logger logger = LogManager.getLogger(Activator.class);

    private volatile BundleContext context;
    private ServiceRegistration serviceRegistration;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        logger.info("Starting Event Generator...");
        //1.create log and publishing event to topic 'bank.events'
        context = bundleContext;
        serviceRegistration = bundleContext.registerService(EventGenerator.class.getName(), new EventGeneratorImpl(), null);

        new EventGeneratorImpl().run();//1.create log and publishing event to topic 'bank.events'

        bundleContext.addServiceListener(this);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        logger.info("Stopping Event Generator...");

        context = null;
        serviceRegistration.unregister();

        bundleContext.removeServiceListener(this);
    }

    @Override
    public void serviceChanged(ServiceEvent serviceEvent) {
//        String[] objectClass = (String[]) serviceEvent.getServiceReference().getProperty(objectClass);

//        if (serviceEvent.getType() == ServiceEvent.REGISTERED)
//        {
//            System.out.println(
//                    Ex1: Service of type + objectClass[0] + &amp;quot; registered.&amp;quot;);
//        }
//        else if (serviceEvent.getType() == ServiceEvent.UNREGISTERING)
//        {
//            System.out.println(
//                    &amp;quot;Ex1: Service of type &amp;quot; + objectClass[0] + &amp;quot; unregistered.&amp;quot;);
//        }
//        else if (serviceEvent.getType() == ServiceEvent.MODIFIED)
//        {
//            System.out.println(
//                    &amp;quot;Ex1: Service of type &amp;quot; + objectClass[0] + &amp;quot; modified.&amp;quot;);
//        }
    }
}
