package com.bigdata.engineer.banking.system.database;

import com.bigdata.engineer.banking.system.Account;
import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.transaction.DepositTransaction;
import com.bigdata.engineer.banking.system.transaction.Transactions;
import com.bigdata.engineer.banking.system.transaction.TransferTransaction;
import com.bigdata.engineer.banking.system.transaction.WithdrawTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDB {
    private static final Logger logger = LogManager.getLogger(BankDB.class);

    private static BankDB instance = new BankDB();
    private Map<String, List<Account>> accountList = new HashMap<>();//customerid, account

    private BankDB () {}

    public static BankDB getInstance() {
        if(instance == null) {
            return instance = new BankDB();
        }
        return instance;
    }

    public Map<String, List<Account>> getAccountList() {
        return accountList;
    }

    public void runTransactions(String customerID, String accountID, Transactions transaction) {
        int balance = 0;
        if(transaction instanceof WithdrawTransaction) {
            balance = transaction.debitAmount(accountID);//출금잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is withdrew AccountID: {}, debitAmount : {}", customerID, accountID, balance);
            }
        } else if(transaction instanceof DepositTransaction) {
            balance = transaction.creditAmount(accountID);//입금잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is Deposited AccountID: {}, creditAmount : {}", customerID, accountID, balance);
            }
        } else if(transaction instanceof TransferTransaction) {
            balance = transaction.debitAmount(accountID);//이체잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "CustomerID '{}' is Transferred AccountID: {}, creditAmount : {}", customerID, accountID, balance);
            }
        }
    }
}
