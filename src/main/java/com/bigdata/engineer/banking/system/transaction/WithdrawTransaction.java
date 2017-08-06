package com.bigdata.engineer.banking.system.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WithdrawTransaction extends Transactions{
    private static final Logger logger = LogManager.getLogger(WithdrawTransaction.class);

    public WithdrawTransaction(int amount) {
        super(amount);
    }

    @Override
    public int debitAmount(String customerID, String accountID) {
        return super.debitAmount(customerID, accountID);
    }//출금잔고
}