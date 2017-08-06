package com.bigdata.engineer.banking.system.exceptions;

import java.io.Serializable;

public class DBAccessException extends RuntimeException implements Serializable{
    private static final long serialVersionUID = 7816880162418711344L;

    public DBAccessException() {
        super("Bank DB access error!!");
    }
}