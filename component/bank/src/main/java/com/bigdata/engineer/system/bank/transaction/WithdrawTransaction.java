package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WithdrawTransaction extends TransactionsImpl {
    private static final Logger logger = LogManager.getLogger(WithdrawTransaction.class);
    private int amount;

    public WithdrawTransaction(String bankID, int amount) {
        super(bankID, amount);
        this.amount = amount;
    }

    @Override
    public int debitAmount(String timestamp, String customerID, String sourceBankID, String sourceAccountID) {
        int balance = super.debitAmount(timestamp, customerID, sourceBankID, sourceAccountID);

//        if (balance - amount > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug(BankingConstants.WITHDRAW_TRANSACTION_LOG_APPENDER + "CustomerID '{}' has withdrew '{}' in '{}' bank '{}' (Before balance '{}' After balance '{}')",
                        customerID,
                        amount,
                        sourceBankID,
                        sourceAccountID,
                        balance,
                        balance - amount);
            }
//        }

        return balance;
    }//출금잔고
}