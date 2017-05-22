package com.rug.energygrid.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;
import com.rug.energygrid.energyProducers.SolarPanel;
import com.rug.energygrid.energyProducers.WindMill;
import com.rug.energygrid.logging.LocalLogger;
import com.rug.energygrid.weather.ExampleDataSet_KNI;
import com.rug.energygrid.weather.Weather;
import jade.util.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//Class used to generate agents to parser, if you don't like to type it by hand
public class Initializer {

    public static Logger logger = LocalLogger.getLogger();

    private Weather weather;
    private List<JSON_Array_Group<JSON_Array_Group<EnergyProducer>>> agentEPList; //TODO: extend
    //private List<List<EnergyConsumer>> energyConsumerList; TODO: implement if we are going to use it
    private List<Cable> cableList;
    private List<AgentArg> prosumerAgents;

    public Initializer(){
      this.agentEPList = new ArrayList();
      this.cableList = new ArrayList();
      this.weather = new ExampleDataSet_KNI();
      initAgents();
      initAllCables();
      initEnergyProducers();

    }

    //Add cables here
    private void initAllCables(){
        cableList.add(new Cable("1111HJ60b", "8748NJ373", 42, 0.3));
        cableList.add(new Cable("9733AB50", "1111HJ60b", 27, 0.7));
        cableList.add(new Cable("9717KH6", "9733AB50", 30, 0.2));
        cableList.add(new Cable("9471KN24", "9717KH6", 8, 0.2));
    }

    //Add Energyproducers here
    private void initEnergyProducers(){
        //for house/agent 9471KN24
        List<JSON_Array_Group> allTypes = new ArrayList<>();
        List<SolarPanel> solarPanelList = new ArrayList<>();
        solarPanelList.add(new SolarPanel(weather));
        solarPanelList.add(new SolarPanel(weather));
        JSON_Array_Group<EnergyProducer> solarpanelGroup = new JSON_Array_Group<>(ConstantsParser.SOLARPANEL_LIST_NAME, solarPanelList);
        List<WindMill> windMillList = new ArrayList<>();
        windMillList.add(new WindMill(weather));
        JSON_Array_Group<EnergyProducer> windMillGroup = new JSON_Array_Group<>(ConstantsParser.WINMILL_LIST_NAME, windMillList);
        allTypes.add(windMillGroup);
        allTypes.add(solarpanelGroup);
        agentEPList.add(new JSON_Array_Group<>("9471KN24", allTypes)); //TODO: maybe we should put the names of the Agents somewhere that case we can use them everywhere

        //for house/agent 9717KH6
        List<JSON_Array_Group> allTypes2 = new ArrayList<>();
        List<WindMill> windMillList2 = new ArrayList<>();
        windMillList2.add(new WindMill(weather));
        JSON_Array_Group<EnergyProducer> windMillGroup2 = new JSON_Array_Group<>(ConstantsParser.WINMILL_LIST_NAME, windMillList);
        allTypes2.add(windMillGroup2);
        agentEPList.add(new JSON_Array_Group<>("9717KH6", allTypes2));
    }

    //add agents here
    private void initAgents(){
        prosumerAgents = new ArrayList<>(); //TODO: store this list in the parser file aswell
        prosumerAgents.add(new AgentArg("9471KN24", -8));
        prosumerAgents.add(new AgentArg("9717KH6", 282));
        prosumerAgents.add(new AgentArg("9733AB50",-32));
        prosumerAgents.add(new AgentArg("1111HJ60b",12));
        prosumerAgents.add(new AgentArg("8748NJ373",18));
    }

    //serializes
    public void build(){

        List<JSON_Array_Group> listOfGroups = new ArrayList<>();
        listOfGroups.add(new JSON_Array_Group(ConstantsParser.CABLE_LIST_NAME, cableList));
        listOfGroups.add(new JSON_Array_Group(ConstantsParser.EP_LIST_NAME, agentEPList));

        Gson g = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(ConstantsParser.JSON_GRID_FILE_LOCATION)) {
            g.toJson(listOfGroups, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            PrintWriter writer = new PrintWriter(ConstantsParser.JSON_AGENT_FILE_LOCATION, "UTF-8");
            writer.println("agents=\\");
            for(int i=0; i<prosumerAgents.size(); i++){
                AgentArg agent = prosumerAgents.get(i);
                writer.println(agent.getAgentName()+":"+ ConstantsParser.PROSUMER_AGENT_CLASS+"("+agent.getArg()+");\\");
            }
            writer.println(ConstantsParser.GLOBAL_TIMER_NAME+":"+ ConstantsParser.GLOBAL_TIMER_AGENT_CLASS+"("+ ConstantsParser.GLOBAL_TIMER_START+","+ ConstantsParser.GLOBAL_TIMER_END+","+ ConstantsParser.GLOBAL_TIMER_SPEEDUP+")");
            writer.println("port="+ ConstantsParser.PORT_NR);
            writer.println("host="+ ConstantsParser.HOST);
            writer.println("main="+ ConstantsParser.MAIN);
            writer.println("no-display="+ ConstantsParser.NO_DISPLAY);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.warning("Initializer can't write agents to file.");
        }

    }

    private class AgentArg {
        private int arg;
        private String agentName;

        public AgentArg(String name, int argument){
            this.agentName = name;
            this.arg = argument;
        }

        public int getArg(){
            return this.arg;
        }

        public String getAgentName(){
            return this.agentName;
        }
    }
}
