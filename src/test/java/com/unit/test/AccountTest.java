//package com.unit.test;
//
//import com.bigdata.engineer.banking.system.Account;
//import com.bigdata.engineer.banking.system.Bank;
//import com.bigdata.engineer.banking.system.exceptions.AccountNotFoundException;
//import com.bigdata.engineer.banking.system.exceptions.InsufficientFundsException;
//import junit.framework.TestCase;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.core.Is.is;
//
//public class AccountTest extends TestCase{
//    private Bank bank;
//
//    @Override
//    protected void setUp() throws Exception {
//        bank = Bank.getInstance();
//    }
//
//    public void testItShouldCreateAccountWithInitialDeposit() throws Exception {
//        bank.createAccount("123", 10);
//        Account account = Account.getAccountID("123");
//        assertThat(account.getBalance(), is(10.0));
//    }
//
//    public void testItShouldTransferMoneyBetweenAccounts() throws Exception {
//        Account a = bank.createAccount("123", 50);
//        Account b = bank.createAccount("456", 75);
//        a.transfer("456", 7);
//        assertThat(a.getBalance(), is(57.0));
//        assertThat(b.getBalance(), is(68.0));
//    }
//
//    public void testItShouldThrowInssuficientFondsWhenTransferMoney() throws Exception {
//        Account a = bank.createAccount("123", 100);
//        bank.createAccount("456", 0);
//        try {
//            a.transfer("456", 10);
//            fail("expected exception not thrown");
//        } catch(InsufficientFundsException e) {
//            assertThat(e.getMessage(), is("Not enough funds!"));
//        }
//    }
//
//    public void testItShouldThrowIfAccountDoesNotExist() throws Exception {
//        Account a = bank.createAccount("123", 100);
//        try {
//            a.transfer("456", 10);
//            fail("expected exception not thrown");
//        } catch(AccountNotFoundException e) {
//            assertThat(e.getMessage(), is("Account '456' does not exist."));
//        }
//    }
//}