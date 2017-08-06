package com.bigdata.engineer.fds.event.sink.publisher.kafka.config;

public class KafkaPublisherConfiguration {

    private String rootPath;
    private String applicationId;
    private String bootstrap;
    private String topic;

	/*------------------------------------------------------------------------------------------*/
    /* 		            Getter and Setter Methods for the private variables                 	*/
    /*------------------------------------------------------------------------------------------*/
    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}