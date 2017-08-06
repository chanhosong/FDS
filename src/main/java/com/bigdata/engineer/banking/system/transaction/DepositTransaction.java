package com.bigdata.engineer.banking.system.transaction;

public class DepositTransaction extends Transactions {

    private int amount;

    public DepositTransaction(int amount) {
        super(amount);
        this.amount = amount;
    }

    @Override
    public int creditAmount(String accountID) {
        return amount;//입금잔고
    }
}