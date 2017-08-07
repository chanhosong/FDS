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
    public int debitAmount(String customerID, String sourceBankID, String sourceAccountID) {
        int balance = super.debitAmount(customerID, sourceBankID, sourceAccountID);
        if (logger.isDebugEnabled()){//Customer has withdrawn 1,000 won from the bank.
            logger.debug(BankingConstants.TRANSACTION_LOG_APPENDER + "CustomerID '{}' has withdrew '${}' in '{}' bank '{}' (Before balance '${}' After balance : '${}')", customerID, amount, sourceBankID, sourceAccountID, balance-amount, balance);
        }
        return balance;
    }//출금잔고
}