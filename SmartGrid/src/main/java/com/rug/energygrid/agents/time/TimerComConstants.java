package com.rug.energygrid.agents.time;

import java.time.Instant;

/**
 * Created by thijs on 9-5-17.
 */
public class TimerComConstants {
    public static final int YELLOW_PAGES_REGISTER_WAIT_TIME = 5000;
    public static final int START_SIMULATION_WAIT_TIME = 5000;

    public static final String TIMER_SD = "TimerSd";
    public static final String CONVERSATION_ID = "InitiateTimerMessage";

    private static final String delimiter = "_";

    public static String timeMessageSerialize(Instant startTime, Instant startSimulationTime, Instant endSimulationTime, int speedup) {
        return ""+startTime+delimiter+startSimulationTime+delimiter+endSimulationTime+delimiter+speedup;
    }

    public static TimeMessageValues timeMessageDeserialize(String message) {
        TimeMessageValues tmv = new TimeMessageValues();
        String[] parts = message.split(delimiter);
        tmv.startTime = Instant.parse(parts[0]);
        tmv.startSimulationTime = Instant.parse(parts[1]);
        tmv.endSimulationTime = Instant.parse(parts[2]);
        tmv.speedup = Integer.parseInt(parts[4]);
        return tmv;
    }

    public static class TimeMessageValues {
        public Instant startTime, startSimulationTime, endSimulationTime;
        public int speedup;
    }
}
