package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.database.BankDB;
import com.bigdata.engineer.banking.system.exceptions.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Transactions {
    private static final Logger logger = LogManager.getLogger(Transactions.class);

    private Map<String, Map<String, Integer>> bankDB = BankDB.getInstance().getBankingData();//customerid, account
    private int amount = 0;

    public Transactions(int amount){
        this.amount = amount;
    }

    public Transactions getTransactionWorkType(String type) {
        switch (type) {
            case BankingConstants.DEPOSIT : return new DepositTransaction(this.amount);
            case BankingConstants.WITHDRAW : return new WithdrawTransaction(this.amount);
            case BankingConstants.TRANSFER : return new TransferTransaction(this.amount);
            default: throw new TransactionException();
        }
    }

    public int creditAmount(String customerID, String accountID) {
        Map<String, Integer> account = bankDB.get(customerID);
        int balance = account.get(accountID);
        int creditAmount = balance+amount;

        account.put(accountID, creditAmount);
        return creditAmount;//입금잔고
    }

    public int debitAmount(String customerID, String accountID) {
        Map<String, Integer> account = bankDB.get(customerID);
        int balance = account.get(accountID);
        int creditAmount = balance-amount;

        account.put(accountID, creditAmount);
        return creditAmount;//출금잔고
    }
}
