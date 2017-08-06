package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.exceptions.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transactions {
    private static final Logger logger = LogManager.getLogger(Transactions.class);

    private int amount = 0;

    public Transactions(int amount){
        this.amount = amount;
    }

    public Transactions getTransactionWorkType(String type) {
        switch (type) {
            case BankingConstants.DEPOSIT : return new DepositTransaction(this.amount);
            case BankingConstants.WITHDRAW : return new WithdrawTransaction(this.amount);
            case BankingConstants.TRANSFER : return new TransferTransaction(this.amount);
            default: throw new TransactionException();
        }
    }

    public int creditAmount(String customerID, String accountID) {
        return 0;
    }//don't use
    public int debitAmount(String customerID, String accountID) {
        return 0;
    }//don't use
}
