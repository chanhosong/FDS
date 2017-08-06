package com.bigdata.engineer.banking.system;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.transaction.Transactions;
import com.bigdata.engineer.banking.system.transaction.WithdrawTransaction;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.bigdata.engineer.banking.system.Account.getBalance;

public class Bank {
    private static final Logger logger = LogManager.getLogger(Bank.class);

    private static Bank instance = new Bank();
    private Map<String, Account> accountList = new HashMap<>();//customerid, account

    private Bank () {
        logger.info(BankingConstants.LOG_APPENDER + "Bank Open!");
    }

    public static Bank getInstance() {
        if(instance == null) {
            return instance = new Bank();
        }
        return instance;
    }

    public Customer createAccount(Customer customer, int initialDeposit) {
        Account account = new Account(initialDeposit);
        customer.setAccountID(accountRegister(customer.getCustomerID(), account));

        if(logger.isDebugEnabled()){
            logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is assigned AccountID: {}, Init Deposit : {}", customer.getCustomerID(), accountList.get(customer.getCustomerID()).getAccountID(), getBalance(account.getAccountID()));
        }

        return customer;
    }

    private String accountRegister(String custormerID, Account account) {
        accountList.put(custormerID, account);
        return account.getAccountID();
    }

    public void runTransactions(String customerID, String accountID, Transactions transaction) {
        int balance = 0;
        if(transaction instanceof WithdrawTransaction) {
            balance = transaction.debitAmount(accountList);//출금잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is withdrew AccountID: {}, debitAmount : {}", customerID, accountID, balance);
            }
        } else {
            balance = transaction.creditAmount(accountList);//입금잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is Deposited AccountID: {}, creditAmount : {}", customerID, accountID, balance);
            }
        }
    }
}