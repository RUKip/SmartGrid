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
    private List<JSON_Array_Group<JSON_Array_Group<EnergyProducer>>> agentEPList;
    private List<Cable> cableList;
    private List<String> prosumerAgents, bigGuyAgents;

    public Initializer(){
      this.agentEPList = new ArrayList();
      this.cableList = new ArrayList();
      this.weather = new ExampleDataSet_KNI();
      initAgents();
      initAllCables();
      initEnergyProducers();

    }

    ////INIT Starts here (you can add/change stuff here)

    //Add cables here
    private void initAllCables(){
        cableList.add(new Cable("1111HJ60b", "8748NJ373", 42, 0.3));
        cableList.add(new Cable("1111HJ60b", "9717KH6", 62, 0.3));
        cableList.add(new Cable("8748NJ373", "9717KH6", 12, 0.3));
        cableList.add(new Cable("9733AB50", "1111HJ60b", 27, 0.7));
        cableList.add(new Cable("9717KH6", "9733AB50", 30, 0.2));
        cableList.add(new Cable("9471KN24", "9717KH6", 8, 0.2));
        cableList.add(new Cable("1111HJ60b", "BigGuyEssent", 1200, 0.2));
        cableList.add(new Cable("9717KH6", "BigGuyEssent", 1040, 0.2));
        cableList.add(new Cable("9733AB50", "BigGuyEssent", 1230, 0.2));
    }

    //Add Energyproducers here (and set settings here ex. windmill.setArea(area))
    private void initEnergyProducers(){
        //for house/agent 9471KN24 (the mixed(solar and wind) house)
        List<JSON_Array_Group> allTypes = new ArrayList<>();
        List<SolarPanel> solarPanelList = new ArrayList<>();
        solarPanelList.add(new SolarPanel(weather));
        solarPanelList.add(new SolarPanel(weather));
        solarPanelList.add(new SolarPanel(weather));
        JSON_Array_Group<EnergyProducer> solarpanelGroup = new JSON_Array_Group<>(ConstantsParser.SOLARPANEL_LIST_NAME, solarPanelList);
        List<WindMill> windMillList = new ArrayList<>();
        windMillList.add(new WindMill(weather));
        JSON_Array_Group<EnergyProducer> windMillGroup = new JSON_Array_Group<>(ConstantsParser.WINMILL_LIST_NAME, windMillList);
        allTypes.add(windMillGroup);
        allTypes.add(solarpanelGroup);
        agentEPList.add(new JSON_Array_Group<>("9471KN24", allTypes)); //TODO: maybe we should put the names of the Agents somewhere that case we can use them everywhere

        //for house/agent 9717KH6 (the windmill house)
        List<JSON_Array_Group> allTypes2 = new ArrayList<>();
        List<WindMill> windMillList2 = new ArrayList<>();
        windMillList2.add(new WindMill(weather));
        windMillList2.add(new WindMill(weather));
        windMillList2.add(new WindMill(weather));
        JSON_Array_Group<EnergyProducer> windMillGroup2 = new JSON_Array_Group<>(ConstantsParser.WINMILL_LIST_NAME, windMillList2);
        allTypes2.add(windMillGroup2);
        agentEPList.add(new JSON_Array_Group<>("9717KH6", allTypes2));

        //for house/agent 8748NJ373 (the solar panel house)
        List<JSON_Array_Group> allTypes3 = new ArrayList<>();
        List<SolarPanel> solarPanelList3 = new ArrayList<>();
        solarPanelList3.add(new SolarPanel(weather));
        solarPanelList3.add(new SolarPanel(weather));
        solarPanelList3.add(new SolarPanel(weather));
        solarPanelList3.add(new SolarPanel(weather));
        solarPanelList3.add(new SolarPanel(weather));
        JSON_Array_Group<EnergyProducer> solarpanelGroup3 = new JSON_Array_Group<>(ConstantsParser.SOLARPANEL_LIST_NAME, solarPanelList3);
        allTypes3.add(solarpanelGroup3);
        agentEPList.add(new JSON_Array_Group<>("8748NJ373", allTypes3));
    }

    //add Agents here
    private void initAgents(){
        //normal houses
        prosumerAgents = new ArrayList<>(); //TODO: store this list in the parser file aswell
        prosumerAgents.add("9471KN24");
        prosumerAgents.add("9717KH6");
        prosumerAgents.add("9733AB50");
        prosumerAgents.add("1111HJ60b");
        prosumerAgents.add("8748NJ373");

        //big guy agents
        bigGuyAgents = new ArrayList<>();
        bigGuyAgents.add("BigGuyEssent");
    }

    /////INIT stops here

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
                String agent = prosumerAgents.get(i);
                writer.println(agent+":"+ ConstantsParser.PROSUMER_AGENT_CLASS+"();\\");
            }
            for(int i=0; i<bigGuyAgents.size(); i++){
                String bigGuy = bigGuyAgents.get(i);
                writer.println(bigGuy+":"+ConstantsParser.BIGGUY_AGENT_CLASS+"();\\");
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
}
