package com.bigdata.engineer.fds.consumer.domain;

public class LogEvent {
    private String type;
    private String timestamp;
    private String customerid;
    private String accountid;
    private String transferaccount;
    private String beforetransferamount;
    private String receivebankname;
    private String receivecustomerid;
    private String creditamount;
    private String debitamount;
    private String transferamount;

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

    public String getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(String creditamount) {
        this.creditamount = creditamount;
    }

    public String getDebitamount() {
        return debitamount;
    }

    public void setDebitamount(String debitamount) {
        this.debitamount = debitamount;
    }

    public String getTransferamount() {
        return transferamount;
    }

    public void setTransferamount(String transferamount) {
        this.transferamount = transferamount;
    }
}
