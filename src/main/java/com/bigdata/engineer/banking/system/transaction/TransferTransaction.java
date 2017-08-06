package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.Account;

import java.util.Map;

public class TransferTransaction extends Transactions{
    private int amount;

    public TransferTransaction(int amount) {
        this.amount = amount;
    }

    @Override
    public int creditAmount(Map<String, Account> accountList) {
        return amount;
    }//입금잔고

    @Override
    public int debitAmount(Map<String, Account> accountList) {
        return amount;
    }//출금잔고
}
