package com.bigdata.engineer.banking.system.database;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.transaction.DepositTransaction;
import com.bigdata.engineer.banking.system.transaction.Transactions;
import com.bigdata.engineer.banking.system.transaction.TransferTransaction;
import com.bigdata.engineer.banking.system.transaction.WithdrawTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BankDB {
    private static final Logger logger = LogManager.getLogger(BankDB.class);

    private String bankID = "";
    private static BankDB instance = new BankDB();
    private Map<String, Map<String, Integer>> bankingData = new HashMap<>();//customerid, account, balance

    private BankDB () {}

    public static BankDB getInstance() {
        if(instance == null) {
            return instance = new BankDB();
        }
        return instance;
    }

    public Map<String, Map<String, Integer>> getBankingData() {
        return bankingData;
    }

    /**
     * Running the transactions.
     *
     * @param customerID
     * @param accountID
     * @param transaction
     */
    public void runTransactions(String customerID, String accountID, Transactions transaction) {
        int balance = 0;
        if(transaction instanceof WithdrawTransaction) {
            balance = transaction.debitAmount(customerID, accountID);//출금잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "'{}' Bank : CustomerID '{}' is withdrew AccountID: {}, debitAmount : {}", bankID, customerID, accountID, balance);
            }
        } else if(transaction instanceof DepositTransaction) {
            balance = transaction.creditAmount(customerID, accountID);//입금잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "'{}' Bank : CustomerID '{}' is Deposited AccountID: {}, creditAmount : {}", bankID, customerID, accountID, balance);
            }
        } else if(transaction instanceof TransferTransaction) {
            balance = transaction.debitAmount(customerID, accountID);//이체잔고
            if (logger.isDebugEnabled()){
                logger.debug(BankingConstants.LOG_APPENDER + "'{}' Bank : CustomerID '{}' is Transferred AccountID: {}, debitAmount : {}", bankID, customerID, accountID, balance);
            }
        }
    }
}
