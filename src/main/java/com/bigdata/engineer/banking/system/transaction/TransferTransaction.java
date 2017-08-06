package com.bigdata.engineer.banking.system.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransferTransaction extends Transactions{
    private static final Logger logger = LogManager.getLogger(TransferTransaction.class);

    private int amount;

    public TransferTransaction(int amount) {
        super(amount);
        this.amount = amount;
    }

    @Override
    public int creditAmount(String customerID, String accountID) {
        return amount;
    }//입금잔고

    @Override
    public int debitAmount(String customerID, String accountID) {
        return amount;
    }//출금잔고
}
