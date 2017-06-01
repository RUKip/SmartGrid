package com.rug.energygrid.parser;

import com.rug.energygrid.logging.LocalLogger;
import jade.util.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgentDeseriaizer {

    public static Logger logger = LocalLogger.getLogger();

    private List<String> agentList = new ArrayList<>();

    public AgentDeseriaizer(){
        deserialize();
    }

    public List<String> getAgentList(){
        return agentList;
    }

    //TODO: make this a singleton, then make this static so that every agent has access
    private void deserialize(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ConstantsParser.JSON_AGENT_FILE_LOCATION));
            Pattern p = Pattern.compile(ConstantsParser.ACCEPTED_AGENT_NAME);

            String line;
            while((line = reader.readLine())!=null) {
                Matcher matcher = p.matcher(line);
                if(matcher.find()){
                    String agent = matcher.group();
                    agentList.add(agent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warning("Something went wrong while tyring to parse the agents");
        }
    }

}
