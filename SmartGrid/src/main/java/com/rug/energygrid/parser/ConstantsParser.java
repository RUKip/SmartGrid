package com.rug.energygrid.parser;

import java.util.regex.Pattern;

/**
 * Created by Ruben on 09-May-17.
 */
public class ConstantsParser {

    public static final String JSON_GRID_FILE_LOCATION = "src/main/resources/grid.json"; // use C:\\grid.json in windows, linus uses /home/etc..
    public static final String JSON_AGENT_FILE_LOCATION = "src/main/resources/jade-agent-container.properties";

    public static final String CABLE_LIST_NAME = "Cables";
    public static final String EP_LIST_NAME = "EnergyProducers";
    public static final String WINMILL_LIST_NAME = "windmills";
    public static final String SOLARPANEL_LIST_NAME = "solarpanels";


    public static final int PORT_NR = 8811;
    public static final String HOST = "localhost";
    public static final boolean MAIN = false;
    public static final boolean NO_DISPLAY = true;

    public static final String PROSUMER_AGENT_CLASS = "com.rug.energygrid.agents.prosumerAgent.ProsumerAgent";
    public static final String GLOBAL_TIMER_AGENT_CLASS = "com.rug.energygrid.agents.time.GlobalTimeAgent";

    public static final String ACCEPTED_AGENT_NAME = "[0-9][0-9][0-9][0-9][A-Z][A-Z][0-9]+[a-z]?";

    public static final String GLOBAL_TIMER_NAME = "globalTimer";
    public static final String GLOBAL_TIMER_START = "1975-01-01T10:15:30.00Z"; //simulation start time
    public static final String GLOBAL_TIMER_END = "1976-01-02T10:15:30.00Z";   //simulation end time
    public static final int GLOBAL_TIMER_SPEEDUP = 14400;

}
