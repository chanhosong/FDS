package com.bigdata.engineer.banking.system;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.database.BankDB;
import com.bigdata.engineer.banking.system.transaction.TransactionsImpl;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import com.bigdata.engineer.event.generator.eventunit.utils.EventOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private static final Logger logger = LogManager.getLogger(Bank.class);

    private String bankID = EventOperations.getCustomerIDGenerator(6,4);
    private Map<String, Map<String, Integer>> bankDB = BankDB.getInstance().getBankingData(bankID);//customerid, account

    public Bank () {
        logger.info(BankingConstants.LOG_APPENDER + "'{}' Bank Open!", bankID);
    }

    public Customer createAccount(Customer customer, int initialDeposit) {
        Account account = new Account(initialDeposit);
        customer.setAccountID(bankID, registerAccountOnBankDB(customer.getCustomerID(), account));

        if(logger.isDebugEnabled()){
            logger.debug(BankingConstants.NEW_ACCOUNTID_LOG_APPENDER + "CustomerID '{}' is assigned AccountID '{}' Bank '{}', Init Deposit '{}'", customer.getCustomerID(), account.getAccountID(), bankID, account.getBalance());
        }

        return customer;
    }

    private String registerAccountOnBankDB(String customerID, Account account) {
        Map<String, Integer> dbAccess = bankDB.get(customerID);

        if(dbAccess == null){
            Map<String, Integer> accountInfo = new HashMap<>();
            accountInfo.put(account.getAccountID(), account.getBalance());
            bankDB.put(customerID, accountInfo);//register customerid and accountid
        } else {//accountid가 이미 있다면
            dbAccess.put(account.getAccountID(), account.getBalance());
        }

        return account.getAccountID();
    }

    public void work(String customerID, String sourceBankID, String sourceAccountID, String targetCustomerID, String targetBankID, String targetAccountID, String work, int amount) {
        BankDB.getInstance().runTransactions(customerID, sourceBankID, sourceAccountID, targetCustomerID, targetBankID, targetAccountID, new TransactionsImpl(targetBankID, amount).getTransactionWorkType(work));
    }
}