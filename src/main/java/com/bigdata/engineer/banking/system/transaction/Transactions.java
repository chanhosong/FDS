package com.bigdata.engineer.banking.system.transaction;

public interface Transactions {
    int creditAmount(String customerID, String sourceBankID, String sourceAccountID);
    int debitAmount(String customerID, String sourceBankID, String sourceAccountID);
    void transferAmount(String customerID, String sourceBankID, String sourceAccountID, String targetBankID, String targetAccountID);
}
