package com.bigdata.engineer.banking.system.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransferTransaction extends Transactions{
    private static final Logger logger = LogManager.getLogger(TransferTransaction.class);

    public TransferTransaction(int amount) {
        super(amount);
    }

    @Override
    public int creditAmount(String customerID, String accountID) {
        return super.creditAmount(customerID, accountID);
    }

    @Override
    public int debitAmount(String customerID, String accountID) {
        return super.debitAmount(customerID, accountID);
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
     * @param customerID
     * @param accountID 송금 대상 계좌번호
     * @return
     */
    public int transferAmount(String customerID, String accountID) {
        return 0;
    }
}
