package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.database.BankDB;
import com.bigdata.engineer.banking.system.exceptions.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class TransactionsImpl implements Transactions {
    private static final Logger logger = LogManager.getLogger(TransactionsImpl.class);

    private Map<String, Map<String, Integer>> bankDB;//customerid, account
    private int amount = 0;
    private String targetBankID = "";

    public TransactionsImpl(String targetBankID, int amount){
        this.amount = amount;
        this.targetBankID = targetBankID;
        bankDB = BankDB.getInstance().getBankingData(targetBankID);
    }

    public TransactionsImpl getTransactionWorkType(String type) {
        switch (type) {
            case BankingConstants.DEPOSIT : return new DepositTransaction(targetBankID, this.amount);
            case BankingConstants.WITHDRAW : return new WithdrawTransaction(targetBankID, this.amount);
            case BankingConstants.TRANSFER : return new TransferTransaction(targetBankID, this.amount);
            default: throw new TransactionException();
        }
    }

    @Override
    public int creditAmount(String customerID, String sourceBankID, String sourceAccountID) {
        Map<String, Integer> account = bankDB.get(customerID);
        int balance = account.get(sourceAccountID);
        int creditAmount = balance + amount;

        account.put(sourceAccountID, creditAmount);
        return creditAmount;//입금잔고
    }

    @Override
    public int debitAmount(String customerID, String sourceBankID, String sourceAccountID) {
        Map<String, Integer> account = bankDB.get(customerID);
        int balance = account.get(sourceAccountID);
        int creditAmount = balance - amount;

        account.put(sourceAccountID, creditAmount);
        return creditAmount;//출금잔고
    }

    @Override
    public void transferAmount(String customerID, String sourceBankID, String sourceAccountID, String targetCustomerID, String targetBankID, String targetAccountID) {}
}
