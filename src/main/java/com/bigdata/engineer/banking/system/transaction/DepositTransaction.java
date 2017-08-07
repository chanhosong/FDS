package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepositTransaction extends TransactionsImpl {
    private static final Logger logger = LogManager.getLogger(DepositTransaction.class);
    private int amount;

    public DepositTransaction(String bankID, int amount) {
        super(bankID, amount);
        this.amount = amount;
    }

    @Override
    public int creditAmount(String customerID, String sourceBankID, String sourceAccountID) {
        int balance = super.creditAmount(customerID, sourceBankID, sourceAccountID);
        if (logger.isDebugEnabled()){
            logger.debug(BankingConstants.TRANSACTION_LOG_APPENDER + "CustomerID '{}' has deposited '${} in '{}' bank '{}' (Before balance '${}' After balance : '${}')", customerID, amount, sourceBankID, sourceAccountID, balance+amount, balance);
        }
        return balance;
    }//입금잔고
}