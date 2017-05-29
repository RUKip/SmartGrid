package com.rug.energygrid.logging;

import jade.util.Logger;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * Created by thijs on 12-5-17.
 */
public class LocalLogger {
    public static Logger logger;
    public static final String FOLDER_NAME = "logs";
    public static final String NAME =  DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneId.systemDefault()).format(Instant.now());

    private synchronized static void initializeLogger() {
        logger = jade.util.Logger.getMyLogger("logger");
        // This block configure the logger with handler and formatter
        try {
            File logsFolder = new File(FOLDER_NAME);
            logsFolder.mkdir();
            FileHandler fh = new FileHandler(FOLDER_NAME + File.separator + NAME +".log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //If you want to use the standard log file.
    public synchronized static Logger getLogger() {
        if (logger == null) {
            initializeLogger();
        }
        return logger;
    }
}
