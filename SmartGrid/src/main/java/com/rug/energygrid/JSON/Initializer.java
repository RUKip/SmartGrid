package com.rug.energygrid.JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;
import com.rug.energygrid.energyProducers.SolarPanel;
import com.rug.energygrid.energyProducers.WindMill;
import com.rug.energygrid.weather.ExampleDataSet_KNI;
import com.rug.energygrid.weather.Weather;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 08-May-17.
 */
public class Initializer {

    private Weather weather;
    private List<JSON_Array_Group<JSON_Array_Group<EnergyProducer>>> agentEPList; //TODO: extend
    //private List<List<EnergyConsumer>> energyConsumerList; TODO: implement if we are going to use it
    private List<Cable> cableList;

    public Initializer(){
      this.agentEPList = new ArrayList();
      this.cableList = new ArrayList();
      this.weather = new ExampleDataSet_KNI();
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
        JSON_Array_Group<EnergyProducer> solarpanelGroup = new JSON_Array_Group<>(ConstantsJSON.SOLARPANEL_LIST_NAME, solarPanelList);
        List<WindMill> windMillList = new ArrayList<>();
        windMillList.add(new WindMill(weather));
        JSON_Array_Group<EnergyProducer> windMillGroup = new JSON_Array_Group<>(ConstantsJSON.WINMILL_LIST_NAME, windMillList);
        allTypes.add(windMillGroup);
        allTypes.add(solarpanelGroup);
        agentEPList.add(new JSON_Array_Group<>("9471KN24", allTypes)); //TODO: maybe we should put the names of the Agents somewhere that case we can use them everywhere

        //for house/agent 9717KH6
        List<JSON_Array_Group> allTypes2 = new ArrayList<>();
        List<WindMill> windMillList2 = new ArrayList<>();
        windMillList.add(new WindMill(weather));
        JSON_Array_Group<EnergyProducer> windMillGroup2 = new JSON_Array_Group<>(ConstantsJSON.WINMILL_LIST_NAME, windMillList);
        allTypes2.add(windMillGroup2);
        agentEPList.add(new JSON_Array_Group<>("9717KH6", allTypes2));
    }

    //serializes
    public void build(){

        List<JSON_Array_Group> listOfGroups = new ArrayList<>();
        listOfGroups.add(new JSON_Array_Group(ConstantsJSON.CABLE_LIST_NAME, cableList));
        listOfGroups.add(new JSON_Array_Group(ConstantsJSON.EP_LIST_NAME, agentEPList));

        Gson g = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(ConstantsJSON.JSON_FILE_LOCATION)) {
            g.toJson(listOfGroups, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }




        //below is for Testing purposes TODO: remove if done testing
        JSON_Deserializer deserializer = new JSON_Deserializer();
        System.out.println("The cables are:");
        List<Cable> cables = deserializer.getCables();
        for(Cable c : deserializer.getCables()) {
            System.out.println("The cable: " + c.getOriginNode() + " to " + c.getConnectedNode() );
        }

        System.out.println("The energy producers of 9471KN24 are:");
        List<EnergyProducer> energyProducers = deserializer.getEnergyProducers("9471KN24");
        for(EnergyProducer e : energyProducers){
            System.out.println(e);
        }
    }
}
