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
    private String accountid;
    private String creditamount;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getCustomerid() {
        return customerid;
    }

    @Override
    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    @Override
    public String getAccountid() {
        return accountid;
    }

    @Override
    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    @Override
    public String getCreditamount() {
        return creditamount;
    }

    @Override
    public void setCreditamount(String creditamount) {
        this.creditamount = creditamount;
    }
}
