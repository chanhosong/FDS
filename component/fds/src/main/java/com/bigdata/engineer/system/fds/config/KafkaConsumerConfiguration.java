package com.bigdata.engineer.system.fds.config;

public class KafkaConsumerConfiguration {

    private String rootPath;
    private String applicationId;
    private String bootstrap;
    private String group;
    private String topic;
    private String offsetReset;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getOffsetReset() {
        return offsetReset;
    }

    public void setOffsetReset(String offsetReset) {
        this.offsetReset = offsetReset;
    }
}