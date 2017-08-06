package com.bigdata.engineer.banking.system;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.database.BankDB;
import com.bigdata.engineer.banking.system.transaction.Transactions;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import com.bigdata.engineer.event.generator.eventunit.utils.EventOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private static final Logger logger = LogManager.getLogger(Bank.class);

    private String bankID = EventOperations.getCustomerIDGenerator(6,4);
    private Map<String, Map<String, Integer>> bankDB = BankDB.getInstance().getBankingData();//customerid, account

    public Bank () {
        logger.info(BankingConstants.LOG_APPENDER + "'{}' Bank Open!", bankID);
    }

    public Customer createAccount(Customer customer, int initialDeposit) {
        Account account = new Account(initialDeposit);
        customer.setAccountID(addBankDB(customer.getCustomerID(), account));

        if(logger.isDebugEnabled()){
            logger.debug(BankingConstants.LOG_APPENDER + "'{}' Bank : CustomerID '{}' is assigned AccountID: {}, Init Deposit : {}", bankID, customer.getCustomerID(), account.getAccountID(), account.getBalance());
        }

        return customer;
    }

    public void work(String customerID, String accountID, String work, int amount) {
        BankDB.getInstance().runTransactions(customerID, accountID, new Transactions(amount).getTransactionWorkType(work));
    }

    private String addBankDB(String custormerID, Account account) {
        if(bankDB.get(custormerID) != null){
            bankDB.get(custormerID).put(account.getAccountID(), account.getBalance());
        } else {
            Map<String, Integer> accountInfo = new HashMap<>();
            accountInfo.put(account.getAccountID(), account.getBalance());
            bankDB.put(custormerID, accountInfo);
        }

        return account.getAccountID();
    }
}