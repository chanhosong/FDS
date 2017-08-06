//package com.bigdata.engineer.banking.system;
//
//import com.bigdata.engineer.banking.system.exceptions.AccountNotFoundException;
//import com.bigdata.engineer.banking.system.transaction.Transaction;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static java.util.Collections.unmodifiableList;
//
//public class MockBank extends Bank {
//
//public Map<String, List<Transaction>> transactionsByAccount = new HashMap<>();
//
//@Override
//public List<Transaction> getTransactions(String accountId) {
//    List<Transaction> transactions = transactionsByAccount.get(accountId);
//    if (transactions == null) {
//        throw new AccountNotFoundException();
//    }
//    return unmodifiableList(transactions);
//}
//
//@Override
//public void runTransactions(String accountId, Transaction transaction) {
//    List<Transaction> transactions = transactionsByAccount.get(accountId);
//    if (transactions == null) {
//        transactions = new ArrayList<>();
//        transactionsByAccount.put(accountId, transactions);
//    }
//    transactions.add(transaction);
//}
//}