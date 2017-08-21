package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.banking.system.database.BankDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;

public class TransferTransaction extends TransactionsImpl {
    private static final Logger logger = LogManager.getLogger(TransferTransaction.class);

    private Map<String, Map<String, Integer>> bankDB;//customerid, account

    private String bankName;
    private int amount = 0;

    public TransferTransaction(String bankID, int amount) {
        super(bankID, amount);
        this.bankName = bankID;
        this.amount = amount;
        bankDB = BankDB.getInstance().getBankingData(bankID);
    }

    /**
     * * 이체 이벤트
     - 발생시각
     - 고객번호
     - 송금 계좌번호
     - 송금 이체전 계좌잔액
     - 수취 은행
     - 수취 계좌주
     - 이체 금액
     */

    @Override
    public void transferAmount(String timestamp, String customerID, String sourceBankID, String sourceAccountID, String targetCustomerID, String targetBankID, String targetAccountID) {
        Map<String, Integer> account = bankDB.get(customerID);
        if(!Objects.equals(account.get(sourceAccountID), null) && !Objects.equals(account.get(targetAccountID), null)) {//본인은행의 다른 계좌 이체
            int debitAmount = super.debitAmount(timestamp, customerID, sourceBankID, sourceAccountID);//1. 먼저 출금을 한다
            int creditAmount = super.creditAmount(timestamp, targetCustomerID, targetBankID, targetAccountID);//2. 다음 입금을 한다

//            if (debitAmount - amount > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug(BankingConstants.TRANSFER_TRANSACTION_LOG_APPENDER +
                                    "CustomerID '{}' transferred '{}' from '{}' bank '{}' (Before balance '{}' After balance '{}') to CustomerID '{}' of '{}' bank '{}' (Before balance '{}' After balance '{}')"
                            ,
                            customerID,
                            amount,
                            sourceBankID,
                            sourceAccountID,
                            debitAmount + amount,
                            debitAmount,
                            targetCustomerID,
                            targetBankID,
                            targetAccountID,
                            creditAmount - amount,
                            creditAmount);
//                }
            }
        }
        else {//타행이체
            int debitAmount = super.debitAmount(timestamp, customerID, targetBankID, sourceAccountID);//1. 먼저 출금을 한다
            int creditAmount = super.creditAmount(timestamp, targetCustomerID, sourceBankID, targetAccountID);//2. 다음 입금을 한다

//            if (debitAmount - amount > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug(BankingConstants.TRANSFER_TRANSACTION_LOG_APPENDER +
                                    "CustomerID '{}' transferred '{}' from '{}' bank '{}' (Before balance '{}' After balance '{}') to CustomerID '{}' of '{}' bank '{}' (Before balance '{}' After balance '{}')"
                            ,
                            customerID,
                            amount,
                            sourceBankID,
                            sourceAccountID,
                            debitAmount + amount,
                            debitAmount,
                            targetCustomerID,
                            targetBankID,
                            targetAccountID,
                            creditAmount - amount,
                            creditAmount);
                }
//            }
        }
    }
}
