package com.bigdata.engineer.event.generator.eventunit.customer;

import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private static final Logger logger = LogManager.getLogger(Customer.class);

    private String customerID;
    private Map<String, String> accountID = new HashMap<>();//bankid, accountid

    public Customer() {
        this.createCustomerID();
    }

    public String getCustomerID() {
        return customerID;
    }

    public Map<String, String> getAccountID() {
        return accountID;
    }

    public String getAccountID(String bankID) {
        return accountID.get(bankID);
    }

    public int getAccountNumber() {
        return accountID.size();
    }

    public void setAccountID(String bankID, String accountID) {
        this.accountID.put(bankID, accountID);
    }

    private void createCustomerID() {
//        this.customerID = EventOperations.randomIdentifier();//EventOperations.getCustomerIDGenerator(30,10);
        this.customerID = new Faker().name().username();
        if(logger.isDebugEnabled()){
            logger.debug(EventConstants.LOG_APPENDER + "New Customer ID : {}", this.customerID);
        }
    }
}