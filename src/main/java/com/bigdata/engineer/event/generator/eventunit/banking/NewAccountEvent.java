package com.bigdata.engineer.event.generator.eventunit.banking;

/**
 * 계좌 신설 이벤트
 - 발생시각
 - 고객번호
 - 계좌번호
 */
public class NewAccountEvent extends LogEvent{
    private String timestamp;
    private String customerID;
    private String accountID;

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
}
