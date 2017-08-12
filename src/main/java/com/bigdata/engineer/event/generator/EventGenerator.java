package com.bigdata.engineer.event.generator;

import com.bigdata.engineer.banking.system.Bank;
import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import com.bigdata.engineer.event.generator.publisher.apps.KafkaPublisherApp;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.bigdata.engineer.banking.system.utils.BankingOperations.getTimestamp;

public class EventGenerator {
    private static final Logger logger = LogManager.getLogger(EventGenerator.class);

    private Map<String, Map<String, Integer>> bankDB;
    private List<Bank> bankList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private static final int numberOfBank = 2;
    private static final int numberOfCustomer = 2;

    public EventGenerator() {
        logger.info(EventConstants.LOG_APPENDER + "EventGenerator is started!");
    }

    public void run() {
        //1.create bank and customer
        this.createBank(numberOfBank);
        this.createCustomer(numberOfCustomer);
        //2.create account: 모든 고객이 각 은행에 대해서 계좌를 1개씩 계설한다
        customerList.forEach(consumer-> bankList.forEach(bank->bank.createAccount(consumer, 0)));
        //3.deposit account: 모든 고객에 대해서 랜덤 보유계좌에 입금한다
        //Random Account : EventOperations.getRandom(0, e.getAccountNumber()-1)
        customerList.forEach(customer->
            customer.getAccountID().keySet().forEach(sourceBankID->bankList.forEach(bank->
                bank.work(
                        getTimestamp(), customer.getCustomerID(), sourceBankID, customer.getAccountID(sourceBankID), null, null, null, BankingConstants.DEPOSIT, new Faker().number().numberBetween(0,2999999)
                )
            )));
        //4.withdraw account: 모든 고객에 대해서 랜덤 보유계좌에 출금한다
        customerList.forEach(customer->
            customer.getAccountID().keySet().forEach(sourceBankID->bankList.forEach(bank->{
//                bankDB = BankDB.getInstance().getBankingData(bank.getBankID());
//                int balance = bankDB.get(customer.getCustomerID()).get(customer.getAccountID(sourceBankID));
                bank.work(
                        getTimestamp(), customer.getCustomerID(), sourceBankID, customer.getAccountID(sourceBankID), null, null, null, BankingConstants.WITHDRAW, new Faker().number().numberBetween(0,2999999)
                );
            })));
        //5.transfer account: 랜덤고객에게 이체한다
        bankList.forEach(targetBank->{
            customerList.forEach(targetCustomer ->{
                customerList.forEach(customer ->
                        customer.getAccountID().keySet().forEach(sourceBankID -> bankList.forEach(bank -> {
//                            bankDB = BankDB.getInstance().getBankingData(sourceBankID);
                            String customerID = customer.getCustomerID();
                            String sourceAccountID = customer.getAccountID(sourceBankID);
                            /*최종 test시에 랜덤 고객을 쓸것*/
    //                        Customer randomCustomer = customerList.get(EventOperations.getRandom(0, customerList.size() - 1));
                            /*전체고객에 대한 이체*/
                            Customer randomCustomer = targetCustomer;
                            String targetCustomerID = randomCustomer.getCustomerID();
                            String targetCustomerAccount = randomCustomer.getAccountID(targetBank.getBankID());
//                            int balance = bankDB.get(customerID).get(sourceAccountID);

                            try {
                                bank.work(
                                        getTimestamp(),
                                        customerID,
                                        sourceBankID,
                                        sourceAccountID,
                                        targetCustomerID,
                                        targetBank.getBankID(),
                                        targetCustomerAccount,
                                        BankingConstants.TRANSFER,
//                                        new Faker().number().numberBetween(0,9999999)
                                        new Faker().number().numberBetween(850000,1050000)
//                                        900000
                                );
                            } catch (Exception e) {
                                if (logger.isTraceEnabled()) {
                                    logger.trace(
                                            "Wrong Transfer Error:: targetCustomerID: {}은 targetBankID: {}에 targetAccountID: {}이 없습니다",
                                            targetCustomerID,
                                            targetBank.getBankID(),
                                            targetCustomerAccount);
                                }
                            }
                        })));
            });
        });


        //6.running log parser and puslishing event
        KafkaPublisherApp kafkaPublisherApp = new KafkaPublisherApp();
        new Thread(kafkaPublisherApp).start();
    }

    private void createBank(int number) {
        IntStream.range(0, number).forEach(e -> bankList.add(new Bank()));
    }

    private void createCustomer(int number) {
        IntStream.range(0, number).forEach(e -> customerList.add(new Customer()));
    }
}