package com.bigdata.engineer.banking.system.database;

import com.bigdata.engineer.banking.system.transaction.DepositTransaction;
import com.bigdata.engineer.banking.system.transaction.TransactionsImpl;
import com.bigdata.engineer.banking.system.transaction.TransferTransaction;
import com.bigdata.engineer.banking.system.transaction.WithdrawTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BankDB {
    private static final Logger logger = LogManager.getLogger(BankDB.class);

    private String bankName = "";
    private static BankDB instance;
    private Map<String, Map<String, Map<String, Integer>>> allBankData = new HashMap<>();//store allBankData
    private Map<String, Map<String, Integer>> bankingData = new HashMap<>();//store customerID, accountID, balance


    private BankDB () {}

    public static BankDB getInstance() {
        if(instance == null) {
            return instance = new BankDB();
        }
        return instance;
    }

    public Map<String, Map<String, Map<String, Integer>>> getAllBankData() {
        return allBankData;
    }

    public Map<String, Map<String, Integer>> getBankingData(String bankID) {
        this.bankName = bankID;
        this.allBankData.put(bankID, bankingData);
        return this.allBankData.get(bankID);
    }

    public void runTransactions(String customerID, String sourceBankID, String sourceAccountID, String targetBankID, String targetAccountID, TransactionsImpl transaction) {
        if(transaction instanceof WithdrawTransaction) {
            transaction.debitAmount(customerID, sourceBankID, sourceAccountID);//출금잔고
        } else if(transaction instanceof DepositTransaction) {
            transaction.creditAmount(customerID, sourceBankID, sourceAccountID);//입금잔고
        } else if(transaction instanceof TransferTransaction) {
            transaction.transferAmount(customerID, sourceBankID, sourceAccountID, targetBankID, targetAccountID);//이체잔고
        }
    }
}
