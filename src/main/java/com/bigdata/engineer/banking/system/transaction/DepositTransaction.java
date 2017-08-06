package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.Account;

import java.util.Map;

public class DepositTransaction extends Transactions {

    private int amount;

    public DepositTransaction(int amount) {
        this.amount = amount;
    }

    @Override
    public int creditAmount(Map<String, Account> accountList) {
    return amount;
    }//입금잔고
}