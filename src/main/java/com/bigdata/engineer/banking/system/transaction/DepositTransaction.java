package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.database.BankDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class DepositTransaction extends Transactions {
    private static final Logger logger = LogManager.getLogger(DepositTransaction.class);

    private Map<String, Map<String, Integer>> bankDB = BankDB.getInstance().getAccountList();//customerid, account
    private int amount;

    public DepositTransaction(int amount) {
        super(amount);
        this.amount = amount;
    }

    @Override
    public int creditAmount(String customerID, String accountID) {
        return amount;//입금잔고
    }
}