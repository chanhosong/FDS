package com.bigdata.engineer.event.generator.log.parser;

import com.bigdata.engineer.banking.system.config.BankingConstants;
import com.bigdata.engineer.event.generator.eventunit.banking.*;
import com.bigdata.engineer.event.generator.eventunit.config.EventConstants;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogParser implements Runnable{
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(LogParser.class);

    private long _updateInterval = 1000;
    private long _filePointer;
    private File _file;
    private static volatile boolean keepRunning = true;
    private File log = new File("src/main/resources/logs/application.log");
    private Map<String, String> logTypeFinder = new HashMap<>();
    private static LinkedList<LogListener> listeners = new LinkedList<>();


    public LogParser(File file) {
        this._file = file;
    }

    private void logTypeRegexInit() {
        logTypeFinder.put(BankingConstants.NEW_ACCOUNTID_LOG_APPENDER, "(CustomerID).(\\'(.*?)\\')(.*)(AccountID).(\\'(.*?)\\')");
        logTypeFinder.put(BankingConstants.DEPOSIT_TRANSACTION_LOG_APPENDER, "(CustomerID).(\\'(.*?)\\')(.*)(deposited).(\\'(.*?)\\')(.*)(bank).(\\'(.*?)\\')");
        logTypeFinder.put(BankingConstants.WITHDRAW_TRANSACTION_LOG_APPENDER, "(CustomerID).(\\'(.*?)\\')(.*)(withdrew).(\\'(.*?)\\')(.*)(bank).(\\'(.*?)\\')");
        logTypeFinder.put(BankingConstants.TRANSFER_TRANSACTION_LOG_APPENDER, "(CustomerID).(\\'(.*?)\\')(.*)(transferred).(\\'(.*?)\\')(.*)(\\'(.*?)\\').(bank).(\\'(.*?)\\')(.*)(Before balance).(\\'(.*?)\\')(.*)(CustomerID).(\\'(.*?)\\')(.*)(\\'(.*?)\\').(bank).(\\'(.*?)\\')");
    }

    public void start() {
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            keepRunning = false;
            try {
                mainThread.join();
            } catch (InterruptedException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug(EventConstants.TAIL_READER_LOG_APPENDER + "Tail reader was interrupted.");
                }
            }
        }));
        LogParser tail = new LogParser(log);
        new Thread(tail).start();
    }

    @Override
    public void run() {
        this.logTypeRegexInit();
        try {
            while (keepRunning) {
                Thread.sleep(_updateInterval);
                long len = _file.length();

                if (len < _filePointer) {
                    // Log must have been jibbled or deleted.
                    if(logger.isDebugEnabled()) {
                        logger.debug("Log file was reset. Restarting logging from start of file.");
                    }
                    _filePointer = len;
                } else if (len > _filePointer) {
                    // File must have had something added to it!
                    RandomAccessFile raf = new RandomAccessFile(_file, "r");
                    raf.seek(_filePointer);
                    String line;
                    while ((line = raf.readLine()) != null) {
                        this.appendTraceLog(line);
                        final String target = line;
                        logTypeFinder.forEach((key, value) -> this.eventDetector(target, key, value));
                    }
                    _filePointer = raf.getFilePointer();
                    raf.close();
                }
            }
        } catch (Exception e) {
            if(logger.isDebugEnabled()) {
                logger.debug("Fatal error reading log file, log tailing has stopped.");
            }
        }
        // dispose();
    }

    private void eventDetector(String targetString, String logType, String regex) {
        Stream.of(targetString)
                .filter(Pattern.compile(logType).asPredicate())
                .forEach(e->{
                    Matcher customerID = Pattern.compile(regex).matcher(e);
                    if (customerID.find()){
                        switch (logType) {
                            case BankingConstants.NEW_ACCOUNTID_LOG_APPENDER :
                                NewAccountEvent n = new NewAccountEvent();
                                n.setType(EventConstants.NEW_ACCOUNT_EVENT_LOG_APPENDER.trim());n.setCustomerID(customerID.group(3));n.setAccountID(customerID.group(7));
                                this.sendEvent(n);
                                logger.debug(EventConstants.NEW_ACCOUNT_EVENT_LOG_APPENDER + "CustomerID '{}' AccountID '{}'", customerID.group(3), customerID.group(7)); break;
                            case BankingConstants.DEPOSIT_TRANSACTION_LOG_APPENDER :
                                DepositEvent d = new DepositEvent();
                                d.setType(EventConstants.DEPOSIT_EVENT_LOG_APPENDER.trim());d.setCustomerID(customerID.group(3));d.setAccountID(customerID.group(11));d.setCreditAmount(customerID.group(7));
                                this.sendEvent(d);
                                logger.debug(EventConstants.DEPOSIT_EVENT_LOG_APPENDER + "CustomerID '{}' AccountID '{}' CreditAmount {}", customerID.group(3), customerID.group(11), customerID.group(7)); break;
                            case BankingConstants.WITHDRAW_TRANSACTION_LOG_APPENDER :
                                WithdrawEvent w = new WithdrawEvent();
                                w.setType(EventConstants.WITHDRAW_EVENT_LOG_APPENDER.trim());w.setCustomerID(customerID.group(3));w.setAccountID(customerID.group(11));w.setDebitAmount(customerID.group(7));
                                this.sendEvent(w);
                                logger.debug(EventConstants.WITHDRAW_EVENT_LOG_APPENDER + "CustomerID '{}' AccountID '{}' DebitAmount {}", customerID.group(3), customerID.group(11), customerID.group(7)); break;
                            case BankingConstants.TRANSFER_TRANSACTION_LOG_APPENDER :
                                TransferEvent t = new TransferEvent();
                                t.setType(EventConstants.TRANSFER_EVENT_LOG_APPENDER.trim());t.setCustomerID(customerID.group(3));t.setTransferAccount(customerID.group(13));t.setBeforeTransferAmount(customerID.group(17));t.setReceiveBankName(customerID.group(24));t.setReceiveCustomerID(customerID.group(21));t.setAmount(customerID.group(7));
                                this.sendEvent(t);
                                logger.debug(EventConstants.TRANSFER_EVENT_LOG_APPENDER + "CustomerID '{}' TransferAccountID '{}' BeforeTransferAmount '{}' ReceiveBankName '{}' ReceiveCustomerID '{}' Amount '{}'", customerID.group(3), customerID.group(13), customerID.group(17), customerID.group(24), customerID.group(21), customerID.group(7)); break;
                        }
                    }
                });
    }

    private void appendTraceLog(String line) {
        if (logger.isTraceEnabled()) {
            logger.trace(line.trim());
        }
    }
    public void addMsgListener(LogListener listener){
        listeners.add(listener);
    }

    public boolean removeMsgListener(LogListener listener){
        return listeners.remove(listener);
    }

    private void sendEvent(LogEvent event) {
        for(LogListener logListener: listeners){
            logListener.onDeliveryMessage(event);
        }
    }
}
