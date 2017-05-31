package com.rug.energygrid.agents.bigGuy;

import com.rug.energygrid.logging.LocalLogger;
import jade.core.AID;
import jade.util.Logger;

import java.awt.dnd.InvalidDnDOperationException;

/**
 * Created by thijs on 31-5-17.
 */
public class BigGuyHolder {
    private static final Logger logger = LocalLogger.getLogger();
    public static AID bigGuy;

    public static void setBigGuy(AID aid) {
        bigGuy = aid;
    }

    public static AID getBigGuy() {
        try {
            if (bigGuy == null)
                throw new Exception("BigGuy not set yer");
        } catch (Exception e) {
            logger.severe("Tried to excess BigGuy, but he was not set yet");
            e.printStackTrace();
        }
        return bigGuy;
    }
}
