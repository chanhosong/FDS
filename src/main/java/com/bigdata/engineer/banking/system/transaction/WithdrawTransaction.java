package com.bigdata.engineer.banking.system.transaction;

public class WithdrawTransaction extends Transactions{

    private final int amount;

    public WithdrawTransaction(int amount) {
        super(amount);
        this.amount = amount;
    }

    @Override
    public int debitAmount(String accountID) {
    return amount;
    }//출금잔고
}