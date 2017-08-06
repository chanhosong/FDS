package com.bigdata.engineer.banking.system;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.database.BankDB;
import com.bigdata.engineer.banking.system.transaction.Transactions;
import com.bigdata.engineer.event.generator.eventunit.customer.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.bigdata.engineer.banking.system.Account.getBalance;

public class Bank {
    private static final Logger logger = LogManager.getLogger(Bank.class);

    private static Bank instance = new Bank();
    private Map<String, List<Account>> bankDB = BankDB.getInstance().getAccountList();//customerid, account

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
        customer.setAccountID(addBankDB(customer.getCustomerID(), account));

        if(logger.isDebugEnabled()){
            logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is assigned AccountID: {}, Init Deposit : {}", customer.getCustomerID(), account.getAccountID(), getBalance(account.getAccountID()));
        }

        return customer;
    }

    public void work(String customerID, String accountID, String work, int amount) {
        BankDB.getInstance().runTransactions(customerID, accountID, new Transactions(amount).getTransactionType(work));
    }

    private String addBankDB(String custormerID, Account account) {
        if(bankDB.get(custormerID) != null){
            bankDB.get(custormerID).add(account);
        } else {
            bankDB.put(custormerID, Collections.singletonList(account));
        }

        return account.getAccountID();
    }
}