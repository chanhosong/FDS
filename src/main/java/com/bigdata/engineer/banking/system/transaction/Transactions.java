package com.bigdata.engineer.banking.system.transaction;

public interface Transactions {
    int creditAmount(String timestamp, String customerID, String sourceBankID, String sourceAccountID);
    int debitAmount(String timestamp, String customerID, String sourceBankID, String sourceAccountID);
    void transferAmount(String timestamp, String customerID, String sourceBankID, String sourceAccountID, String targetCustomerID, String targetBankID, String targetAccountID);
}
