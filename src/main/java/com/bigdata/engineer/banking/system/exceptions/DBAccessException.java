package com.bigdata.engineer.banking.system.exceptions;

public class DBAccessException extends RuntimeException {
  private static final long serialVersionUID = 8865462980318073091L;

  public DBAccessException() {
    super("If you getAccountID here then it means that your test is talking to the DB, "
        + "which means that you failed in isolating the code properly.");
  }
}