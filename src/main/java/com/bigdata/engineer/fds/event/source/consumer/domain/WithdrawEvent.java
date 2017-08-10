package com.bigdata.engineer.fds.event.source.consumer.domain;

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

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getDebitamount() {
        return debitamount;
    }

    public void setDebitamount(String debitamount) {
        this.debitamount = debitamount;
    }
}
