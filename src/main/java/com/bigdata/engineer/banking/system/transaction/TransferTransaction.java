package com.bigdata.engineer.banking.system.transaction;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransferTransaction extends TransactionsImpl {
    private static final Logger logger = LogManager.getLogger(TransferTransaction.class);

    private String bankName;
    private int amount = 0;

    public TransferTransaction(String bankID, int amount) {
        super(bankID, amount);
        this.bankName = bankID;
        this.amount = amount;
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
    public void transferAmount(String customerID, String sourceBankID, String sourceAccountID, String targetBankID, String targetAccountID) {
        int debitAmount = super.debitAmount(customerID, sourceBankID, sourceAccountID);//1. 먼저 출금을 한다
        int creditAmount = super.creditAmount(customerID, targetBankID, targetAccountID);//2. 다음 입금을 한다

        if (logger.isDebugEnabled()){
//                logger.debug(BankingConstants.TRANSACTION_LOG_APPENDER + "CustomerID '{}' transferred '${}' from '{}' bank '{}' (Before balance '${}' After balance : '{}') to '{}' bank '{}' (Before balance '{}' After balance : '{}')", customerID, amount, sourceBankID, sourceAccountID, this.bankName, targetAccountID, balance);
            logger.debug(BankingConstants.TRANSACTION_LOG_APPENDER +
                    "CustomerID '{}' transferred '${}' from '{}' bank '{}' (Before balance '${}' After balance : '${}') to '{}' bank '{}' (Before balance '${}' After balance : '${}')"
                    , customerID, amount, sourceBankID, sourceAccountID, debitAmount+amount, debitAmount, this.bankName, targetAccountID, creditAmount-amount, creditAmount);
        }
    }
}
