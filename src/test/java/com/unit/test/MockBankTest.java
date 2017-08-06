//package com.unit.test;
//
//import com.bigdata.engineer.banking.system.MockBank;
//import com.bigdata.engineer.banking.system.transaction.DepositTransaction;
//import com.bigdata.engineer.banking.system.transaction.Transaction;
//import junit.framework.TestCase;
//
//import static java.util.Arrays.asList;
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
//public class MockBankTest extends TestCase {
//    MockBank bank = new MockBank();
//
//    public void testItShouldAddAndRetrieveTransaction() throws Exception {
//        Transaction tx = new DepositTransaction(0);
//        bank.runTransactions("123", tx);
//        assertThat(bank.getTransactions("123"), is(asList(tx)));
//    }
//
//    public void testItShouldAddMultipleAndRetrieveInSameOrder() throws Exception {
//        Transaction tx1 = new DepositTransaction(1);
//        Transaction tx2 = new DepositTransaction(2);
//        Transaction tx3 = new DepositTransaction(3);
//        bank.runTransactions("123", tx1);
//        bank.runTransactions("123", tx2);
//        bank.runTransactions("456", tx3); // make sure we have separate lists
//        assertThat(bank.getTransactions("123"), is(asList(tx1, tx2)));
//
//    }
//}