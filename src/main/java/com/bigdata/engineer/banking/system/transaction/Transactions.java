package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.exceptions.DBAccessException;

public class Transactions {
    private int amount = 0;

    public Transactions(int amount){
        this.amount = amount;
    }

    public Transactions getTransactionWorkType(String type) {
        switch (type) {
            case BankingConstants.DEPOSIT : return new DepositTransaction(this.amount);
            case BankingConstants.WITHDRAW : return new WithdrawTransaction(this.amount);
            case BankingConstants.TRANSFER : return new TransferTransaction(this.amount);
            default: throw new DBAccessException();
        }
    }

    public int creditAmount(String accountID) {
        return 0;
    }
    public int debitAmount(String accountID) {
        return 0;
    }
}
