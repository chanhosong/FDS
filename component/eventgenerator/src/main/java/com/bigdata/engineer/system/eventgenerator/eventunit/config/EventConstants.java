package com.bigdata.engineer.system.eventgenerator.eventunit.config;

public class EventConstants {
    public static final String TARGER_LOG_LOCATION = "component/bank/src/main/resources/logs/application.log";
    public static final String LOG_APPENDER = "CUSTOMER_LOG:: ";
    public static final String TAIL_READER_LOG_APPENDER = "TAIL_READER_LOG:: ";

    /*------------------------------------------------------------------------------------------*/
    /* 		                          Bank Event Log Type                                      	*/
    /*------------------------------------------------------------------------------------------*/
    public static final String NEW_ACCOUNT_EVENT_LOG_APPENDER = "NEW_ACCOUNT_EVENT_LOG:: ";
    public static final String DEPOSIT_EVENT_LOG_APPENDER = "DEPOSIT_EVENT_LOG:: ";
    public static final String WITHDRAW_EVENT_LOG_APPENDER = "WITHDRAW_EVENT_LOG:: ";
    public static final String TRANSFER_EVENT_LOG_APPENDER = "TRANSFER_EVENT_LOG:: ";

    /*------------------------------------------------------------------------------------------*/
    /* 		                       Bank Transaction Log Type                                   	*/
    /*------------------------------------------------------------------------------------------*/
    public static final String NEW_ACCOUNTID_LOG_APPENDER = "NEW_ACCOUNTID_LOG:: ";
    public static final String DEPOSIT_TRANSACTION_LOG_APPENDER = "DEPOSIT_TRANSACTION_LOG:: ";
    public static final String TRANSFER_TRANSACTION_LOG_APPENDER = "TRANSFER_TRANSACTION_LOG:: ";
    public static final String WITHDRAW_TRANSACTION_LOG_APPENDER = "WITHDRAW_TRANSACTION_LOG:: ";
}
