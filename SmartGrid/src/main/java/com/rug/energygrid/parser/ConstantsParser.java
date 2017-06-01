package com.rug.energygrid.parser;

import java.io.File;
import java.util.regex.Pattern;

public class ConstantsParser {

    public static final String JSON_GRID_FILE_LOCATION = "src" + File.separator + "main" + File.separator + "resources" +File.separator+ "grid.json"; // use C:\\grid.json in windows, linus uses /home/etc..
    public static final String JSON_AGENT_FILE_LOCATION = "src" + File.separator+ "main" + File.separator + "resources" + File.separator + "jade-agent-container.properties";

    public static final String CABLE_LIST_NAME = "Cables";
    public static final String EP_LIST_NAME = "EnergyProducers";
    public static final String WINMILL_LIST_NAME = "windmills";
    public static final String SOLARPANEL_LIST_NAME = "solarpanels";


    public static final int PORT_NR = 8811;
    public static final String HOST = "localhost";
    public static final boolean MAIN = false;
    public static final boolean NO_DISPLAY = true;

    public static final String PROSUMER_AGENT_CLASS = "com.rug.energygrid.agents.prosumerAgent.ProsumerAgent";
    public static final String BIGGUY_AGENT_CLASS = "com.rug.energygrid.agents.bigGuyAgent.BigGuyAgent";
    public static final String GLOBAL_TIMER_AGENT_CLASS = "com.rug.energygrid.agents.time.GlobalTimeAgent";

    public static final String ACCEPTED_AGENT_NAME = "(BigGuy[A-Za-z]*)|([0-9][0-9][0-9][0-9][A-Z][A-Z][0-9]+[a-z]?)";

    public static final String GLOBAL_TIMER_NAME = "globalTimer";

    public static final String GLOBAL_TIMER_START = "1995-01-01T10:15:30.00Z"; //simulation start time
    public static final String GLOBAL_TIMER_END = "1995-03-01T10:15:30.00Z";   //simulation end time
    public static final int GLOBAL_TIMER_SPEEDUP = 85200;
}
