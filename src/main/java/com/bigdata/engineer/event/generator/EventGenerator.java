package com.bigdata.engineer.event.generator;

import com.bigdata.engineer.banking.system.Bank;
import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import com.bigdata.engineer.event.generator.eventunit.utils.EventOperations;
import com.bigdata.engineer.fds.event.sink.publisher.apps.KafkaPublisherApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EventGenerator {
    private static final Logger logger = LogManager.getLogger(EventGenerator.class);

    private String timeStamp = EventOperations.getTimestamp();
    private List<Bank> bankList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private static final int numberOfBank = 3;
    private static final int numberOfCustomer = 3;

    public EventGenerator() {
        logger.info(EventConstants.LOG_APPENDER + "EventGenerator is started!");
    }

    public void run() {
        //1.create bank and customer
        this.createBank(numberOfBank);
        this.createCustomer(numberOfCustomer);
        //2.create account: 모든 고객이 각 은행에 대해서 계좌를 1개씩 계설한다
        customerList.forEach(consumer-> bankList.forEach(bank->bank.createAccount(consumer, EventOperations.getRandom(0,10000))));
        //3.deposit account: 모든 고객에 대해서 랜덤 보유계좌에 입금한다
        //Random Account : EventOperations.getRandom(0, e.getAccountNumber()-1)
        customerList.forEach(customer->
            customer.getAccountID().keySet().forEach(sourceBankID->bankList.forEach(bank->bank.work(
                customer.getCustomerID(), sourceBankID, customer.getAccountID(sourceBankID), null, null, null, BankingConstants.DEPOSIT, 1000
            ))));
        //4.withdraw account: 모든 고객에 대해서 랜덤 보유계좌에 출금한다
        customerList.forEach(customer->
            customer.getAccountID().keySet().forEach(sourceBankID->bankList.forEach(bank->bank.work(
                customer.getCustomerID(), sourceBankID, customer.getAccountID(sourceBankID), null, null, null, BankingConstants.WITHDRAW, 1000
            ))));
        //5.transfer account: 랜덤고객에게 이체한다
        customerList.forEach(customer ->
                customer.getAccountID().keySet().forEach(sourceBankID -> bankList.forEach(bank -> {
                    String customerID = customer.getCustomerID();
                    String sourceAccountID = customer.getAccountID(sourceBankID);
                    Customer randomCustomer = customerList.get(EventOperations.getRandom(0,
                            customerList.size() - 1));
                    String targetCustomerID = randomCustomer.getCustomerID();
                    String targetCustomerAccount = randomCustomer.getAccountID(sourceBankID);

                    try {
                        bank.work(
                                customerID,
                                sourceBankID,
                                sourceAccountID,
                                targetCustomerID,
                                sourceBankID,//TODO change to targetBankID
                                targetCustomerAccount,
                                BankingConstants.TRANSFER,
                                1000
                        );
                    } catch (Exception e) {
                        if (logger.isTraceEnabled()) {
                            logger.trace(
                                    "Wrong Transfer Error:: targetCustomerID: {}은 targetBankID: {}에 targetAccountID: {}이 없습니다",
                                    targetCustomerID,
                                    sourceBankID,
                                    targetCustomerAccount);
                        }
                    }
                })));

        //6.running log parser
        KafkaPublisherApp kafkaPublisherApp = new KafkaPublisherApp();
        kafkaPublisherApp.run();
    }

    private void createBank(int number) {
        IntStream.range(0, number).forEach(e -> bankList.add(new Bank()));
    }

    private void createCustomer(int number) {
        IntStream.range(0, number).forEach(e -> customerList.add(new Customer()));
    }
}