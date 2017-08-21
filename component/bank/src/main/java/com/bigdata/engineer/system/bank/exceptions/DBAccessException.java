package com.bigdata.engineer.system.bank.exceptions;

public class DBAccessException extends RuntimeException {
    private static final long serialVersionUID = 7816880162418711344L;

    public DBAccessException() {
        super("Bank DB access error!!");
    }
}