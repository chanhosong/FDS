package com.bigdata.engineer.banking.system;

import com.bigdata.engineer.banking.system.utils.BankingOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Account {
    private static final Logger logger = LogManager.getLogger(Account.class);

    private String accountID = "";
    private int balance = 0;

    public Account(int initialDeposit) {
        this.createAccount(initialDeposit);
    }

    private void createAccount(int initialDeposit) {
        this.accountID = BankingOperations.getAccountIDGenerator(30, 4);
        this.balance = initialDeposit;
    }

    public String getAccountID() {
        return accountID;
    }

    public int getBalance() {
        return this.balance;
    }
}