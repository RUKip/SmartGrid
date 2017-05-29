package com.rug.energygrid;

import com.rug.energygrid.gatherData.GatherData;

/**
 * Created by thijs on 22-5-17.
 */
public class FinishedChecker {
    private static int counter = 0;

    public static synchronized void agentAdded() {
        counter++;
    }

    public static synchronized void agentRemoved() {
        counter--;
        System.out.println("counter: "+counter);
        if (counter < 1) {
            GatherData.GATHER_DATA.createOutput();
            System.exit(-1);
        }
    }
}
