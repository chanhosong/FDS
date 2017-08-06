package com.bigdata.engineer.event.generator;

import com.bigdata.engineer.banking.system.Bank;
import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.event.generator.eventunit.config.CustomerConstants;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import com.bigdata.engineer.event.generator.eventunit.utils.EventOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EventGenerator {
    private static final Logger logger = LogManager.getLogger(EventGenerator.class);
    private static final Bank bank = Bank.getInstance();

    private String timeStamp = EventOperations.getTimestamp();
    private List<Customer> customerList = new ArrayList<>();

    public void init() {
        if(logger.isDebugEnabled()){
            logger.debug(CustomerConstants.LOG_APPENDER + "EventGenerator is started!");
        }
        //1.create customer
        this.createCustomer(3);
        //2.create account: 모든 고객에 대해서 계좌를 1개씩 계설한다
        customerList.forEach(e-> bank.createAccount(e, EventOperations.getRandom(0,10000)));
        //3.deposit account: 모든 고객에 대해서 랜덤 보유계좌에 입금한다
        customerList.forEach(e-> bank.work(
                e.getCustomerID(),
                e.getAccountID(e.getAccountNumber() == 0 ? EventOperations.getRandom(0, e.getAccountNumber()) : 0),
                BankingConstants.DEPOSIT,1000)
        );
    }

    private void createCustomer(int number) {
        IntStream.range(0, number).forEach(e -> customerList.add(new Customer()));
    }
}