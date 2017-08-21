package com.bigdata.engineer.system.fds.domain;

public class FraudDetectionEvent extends LogEvent {
    private String type;
    private String timestamp;
    private String customerid;
    private String transferaccount;
    private String beforetransferamount;
    private String receivebankname;
    private String receivecustomerid;
    private String creditamount;
    private String transferamount;
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
