package com.rug.energygrid;

import com.rug.energygrid.gatherData.GatherData;
import com.rug.energygrid.logging.LocalLogger;
import jade.util.Logger;

public class FinishedChecker {
    private static int counter = 0;

    public static synchronized void agentAdded() {
        counter++;
    }

    private static final Logger logger = LocalLogger.getLogger();

    public static synchronized void agentRemoved() {
        counter--;
        if (counter < 1) {
            GatherData.GATHER_DATA.createOutput();
            logger.info("System finished properly");
            System.exit(-1);
        }
    }
}
