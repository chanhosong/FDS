package com.bigdata.engineer.fds.event.source.consumer.domain;

/**
 * 입금 이벤트
 - 발생시각
 - 고객번호
 - 계좌번호
 - 입금 금액
 */
public class DepositEvent extends LogEvent {
    private String type;
    private String timestamp;
    private String customerid;
    private String accountID;
    private String creditamount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(String creditAccount) {
        this.creditamount = creditAccount;
    }
}
