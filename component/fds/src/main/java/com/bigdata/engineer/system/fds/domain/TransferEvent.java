package com.bigdata.engineer.fds.consumer.domain;

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
    private String transferamount;

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
    public String getTransferaccount() {
        return transferaccount;
    }

    @Override
    public void setTransferaccount(String transferaccount) {
        this.transferaccount = transferaccount;
    }

    @Override
    public String getBeforetransferamount() {
        return beforetransferamount;
    }

    @Override
    public void setBeforetransferamount(String beforetransferamount) {
        this.beforetransferamount = beforetransferamount;
    }

    @Override
    public String getReceivebankname() {
        return receivebankname;
    }

    @Override
    public void setReceivebankname(String receivebankname) {
        this.receivebankname = receivebankname;
    }

    @Override
    public String getReceivecustomerid() {
        return receivecustomerid;
    }

    @Override
    public void setReceivecustomerid(String receivecustomerid) {
        this.receivecustomerid = receivecustomerid;
    }

    @Override
    public String getTransferamount() {
        return transferamount;
    }

    @Override
    public void setTransferamount(String transferamount) {
        this.transferamount = transferamount;
    }
}
