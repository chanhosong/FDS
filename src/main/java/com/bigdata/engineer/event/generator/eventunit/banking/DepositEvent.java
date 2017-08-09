package com.bigdata.engineer.event.generator.eventunit.banking;

/**
 * 입금 이벤트
 - 발생시각
 - 고객번호
 - 계좌번호
 - 입금 금액
 */
public class DepositEvent extends LogEvent{
    private String timestamp;
    private String customerID;
    private String accountID;
    private String creditAmount;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAccount) {
        this.creditAmount = creditAccount;
    }
}
