package com.bigdata.engineer.event.generator.eventunit.banking;

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
public class TransferEvent extends LogEvent{
    private String timestamp;
    private String customerID;
    private String transferAccount;
    private String beforeTransferAmount;
    private String receiveBankName;
    private String receiveCustomerID;
    private String amount;

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

    public String getTransferAccount() {
        return transferAccount;
    }

    public void setTransferAccount(String transferAccount) {
        this.transferAccount = transferAccount;
    }

    public String getBeforeTransferAmount() {
        return beforeTransferAmount;
    }

    public void setBeforeTransferAmount(String beforeTransferAmount) {
        this.beforeTransferAmount = beforeTransferAmount;
    }

    public String getReceiveBankName() {
        return receiveBankName;
    }

    public void setReceiveBankName(String receiveBankName) {
        this.receiveBankName = receiveBankName;
    }

    public String getReceiveCustomerID() {
        return receiveCustomerID;
    }

    public void setReceiveCustomerID(String receiveCustomerID) {
        this.receiveCustomerID = receiveCustomerID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
