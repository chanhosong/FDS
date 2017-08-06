package com.bigdata.engineer.banking.system.transaction;

public class TransferTransaction extends Transactions{
    private int amount;

    public TransferTransaction(int amount) {
        super(amount);
        this.amount = amount;
    }

    @Override
    public int creditAmount(String accountID) {
        return amount;
    }//입금잔고

    @Override
    public int debitAmount(String accountID) {
        return amount;
    }//출금잔고
}
