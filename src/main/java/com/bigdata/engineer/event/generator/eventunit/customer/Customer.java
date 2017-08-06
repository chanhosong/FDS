package com.bigdata.engineer.event.generator.eventunit.customer;

import com.bigdata.engineer.event.generator.eventunit.config.CustomerConstants;
import com.bigdata.engineer.event.generator.eventunit.utils.EventOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private static final Logger logger = LogManager.getLogger(Customer.class);

    private String customerID;
    private List<String> accountID = new ArrayList<>();

    public Customer() {
        this.createCustomerID();
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAccountID(int index) {
        return accountID.get(index);
    }

    public int getAccountNumber() {
        return accountID.size();
    }

    public void setAccountID(String accountID) {
        this.accountID.add(accountID);
    }

    private void createCustomerID() {
        this.customerID = EventOperations.getCustomerIDGenerator(30,4);
        if(logger.isDebugEnabled()){
            logger.debug(CustomerConstants.LOG_APPENDER + "New Customer ID : {}", this.customerID);
        }
    }
}