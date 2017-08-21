package com.bigdata.engineer.system.bank.exceptions;

public class TransactionException extends RuntimeException{
    private static final long serialVersionUID = 2944689788691231398L;

    public TransactionException() {
        super("An error occurred while processing the transaction.");
    }
}
