package com.rug.energygrid.logging;

import jade.core.Agent;
import jade.util.Logger;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * Created by thijs on 12-5-17.
 */
public class LocalLogger {
    public static Logger logger;
    public static final String folderName = "logs";
    public static final Instant name = Instant.now();

    private synchronized static void initializeLogger() {
        logger = jade.util.Logger.getMyLogger("logger");
        // This block configure the logger with handler and formatter
        try {
            FileHandler fh = new FileHandler(folderName+"/"+name+".log");
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
