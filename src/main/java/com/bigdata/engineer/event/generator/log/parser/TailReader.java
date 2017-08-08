package com.bigdata.engineer.event.generator.log.parser;

import com.bigdata.engineer.event.generator.eventunit.config.CustomerConstants;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TailReader implements Runnable{
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TailReader.class);

    private long _updateInterval = 1000;
    private long _filePointer;
    private File _file;
    private static volatile boolean keepRunning = true;
    private File log = new File("src/main/resources/logs/application.log");

    public TailReader(File file) {
        this._file = file;
    }

    public void init () {
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            keepRunning = false;
            try {
                mainThread.join();
            } catch (InterruptedException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug(CustomerConstants.TATILREADER_LOG_APPENDER + "Tail Reader was Interrupted.");
                }
            }
        }));
        TailReader tail = new TailReader(log);
        new Thread(tail).start();
    }

    @Override
    public void run() {
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
                        this.appendLine(line);
                        //TODO Event gen.
                        //BANKING_SYSTEM_LOG:: '303' Bank : CustomerID '202001313221010' is assigned AccountID: 2221211213021-221131011121212, Init Deposit : 5037
                        Stream.of(line).filter(Pattern.compile("AccountID").asPredicate()).forEach(e->e.split(" "));
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

    private void appendLine(String line) {
        if (logger.isTraceEnabled()) {
            logger.trace(line.trim());
        }
    }
}
