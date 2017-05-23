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
        if (counter < 1) {
            System.out.println("Counter is 0 simulation is finished");
            GatherData.GATHER_DATA.createOutput();
            System.exit(-1);
        }
    }
}
