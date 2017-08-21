package com.bigdata.engineer.system.fds.domain;

/**
 * 출금 이벤트
 - 발생시각
 - 고객번호
 - 계좌번호
 - 출금 금액
 */
public class WithdrawEvent extends LogEvent {
    private String type;
    private String timestamp;
    private String customerid;
    private String accountid;
    private String debitamount;

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
    public String getDebitamount() {
        return debitamount;
    }

    @Override
    public void setDebitamount(String debitamount) {
        this.debitamount = debitamount;
    }
}
