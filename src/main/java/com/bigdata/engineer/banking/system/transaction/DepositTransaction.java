package com.bigdata.engineer.banking.system.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepositTransaction extends Transactions {
    private static final Logger logger = LogManager.getLogger(DepositTransaction.class);
    private int amount;

    public DepositTransaction(int amount) {
        super(amount);
    }

    public int creditAmount(String customerID, String accountID) {
        return super.creditAmount(customerID, accountID);
    }
}