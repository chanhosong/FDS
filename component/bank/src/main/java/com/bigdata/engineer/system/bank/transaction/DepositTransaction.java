package com.bigdata.engineer.system.bank.transaction;

import com.bigdata.engineer.system.bank.config.BankingConstants;
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
    public int creditAmount(String timestamp, String customerID, String sourceBankID, String sourceAccountID) {
        int balance = super.creditAmount(timestamp, customerID, sourceBankID, sourceAccountID);

//        if (balance > 0 && balance + amount > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug(BankingConstants.DEPOSIT_TRANSACTION_LOG_APPENDER + "CustomerID '{}' has deposited '{}' in '{}' bank '{}' (Before balance '{}' After balance '{}')",
                        customerID,
                        amount,
                        sourceBankID,
                        sourceAccountID,
                        balance,
                        balance + amount);
            }
//        }

        return balance;
    }//입금잔고
}