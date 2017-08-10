package com.bigdata.engineer.fds.event.source.consumer.domain;

/**
 * 이체 이벤트
 - 발생시각
 - 고객번호
 - 송금 계좌번호
 - 송금 이체전 계좌잔액
 - 수취 은행
 - 수취 계좌주
 - 이체 금액
 */
public class TransferEvent extends LogEvent {
    private String type;
    private String timestamp;
    private String customerid;
    private String transferaccount;
    private String beforetransferamount;
    private String receivebankname;
    private String receivecustomerid;
    private String amount;

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

    public String getTransferaccount() {
        return transferaccount;
    }

    public void setTransferaccount(String transferaccount) {
        this.transferaccount = transferaccount;
    }

    public String getBeforetransferamount() {
        return beforetransferamount;
    }

    public void setBeforetransferamount(String beforetransferamount) {
        this.beforetransferamount = beforetransferamount;
    }

    public String getReceivebankname() {
        return receivebankname;
    }

    public void setReceivebankname(String receivebankname) {
        this.receivebankname = receivebankname;
    }

    public String getReceivecustomerid() {
        return receivecustomerid;
    }

    public void setReceivecustomerid(String receivecustomerid) {
        this.receivecustomerid = receivecustomerid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
