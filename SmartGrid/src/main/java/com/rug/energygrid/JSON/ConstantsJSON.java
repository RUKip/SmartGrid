package com.rug.energygrid.JSON;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;

/**
 * Created by Ruben on 09-May-17.
 */
public class ConstantsJSON {

    public static final String JSON_GRID_FILE_LOCATION = "grid.json"; // use C:\\grid.json in windows, linus uses /home/etc..
    public static final String JSON_AGENT_FILE_LOCATION = "jade-agent-container.properties";

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

    public static final String GLOBAL_TIMER_NAME = "globalTimer";
    public static final String GLOBAL_TIMER_ARG1 = "";
    public static final String GLOBAL_TIMER_ARG2 = "";
    public static final int GLOBAL_TIMER_SPEEDUP = 14400;

}
