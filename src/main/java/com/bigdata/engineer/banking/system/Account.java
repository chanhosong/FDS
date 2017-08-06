package com.bigdata.engineer.banking.system;

import com.bigdata.engineer.banking.system.transaction.Transactions;
import com.bigdata.engineer.banking.system.utils.AccountOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private static final Logger logger = LogManager.getLogger(Account.class);

    private static final Map<String, Integer> accountData = new HashMap<>();//accountid and balance
    private String accountID = "";

    public Account(int initialDeposit) {
        this.createAccount(initialDeposit);
    }

    private void createAccount(int initialDeposit) {
        this.accountID = AccountOperations.getAccountIDGenerator(30, 4);
        this.accountData.put(accountID, initialDeposit);
    }

    public String getAccountID() {
        return accountID;
    }

    public static int getBalance(String accountID) {
        return accountData.get(accountID);
    }

//    public double getBalance() {
//        double balance = 0;
//        for (Transaction transaction : Bank.getInstance().getTransactions(accountData)) {
//          balance += transaction.creditAmount() - transaction.debitAmount();
//        }
//        return balance;
//    }

    public void addTransaction(Transactions transactions) {
//      Bank.getInstance().runTransactions(, transaction);
    }

//    public void transfer(String fromAccountId, double amount) {
//        Account fromAccount = Account.getAccountID(fromAccountId);
//        addTransaction(new WithdrawTransaction(amount));
//        fromAccount.addTransaction(new DepositTransaction(amount));
//    }
}