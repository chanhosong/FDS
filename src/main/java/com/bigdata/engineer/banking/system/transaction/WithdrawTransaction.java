package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.Account;

import java.util.Map;

public class WithdrawTransaction extends Transactions{

    private final int amount;

    public WithdrawTransaction(int amount) {
        this.amount = amount;
    }

    @Override
    public int debitAmount(Map<String, Account> accountList) {
    return amount;
    }//출금잔고
}