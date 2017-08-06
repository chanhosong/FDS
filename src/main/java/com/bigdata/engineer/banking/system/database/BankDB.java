package com.bigdata.engineer.banking.system.database;

import com.bigdata.engineer.banking.system.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDB {
    private static final Logger logger = LogManager.getLogger(BankDB.class);

    private static BankDB instance = new BankDB();
    private Map<String, List<Account>> accountList = new HashMap<>();//customerid, account

    private BankDB () {}

    public static BankDB getInstance() {
        if(instance == null) {
            return instance = new BankDB();
        }
        return instance;
    }

    public Map<String, List<Account>> getAccountList() {
        return accountList;
    }
}
