package com.rug.energygrid.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;
import com.rug.energygrid.energyProducers.SolarPanel;
import com.rug.energygrid.energyProducers.WindMill;
import com.rug.energygrid.logging.LocalLogger;
import jade.util.Logger;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSON_Grid_Deserializer {

    private JSON_Array_Group cableGroup;
    private JSON_Array_Group energyProducerGroup;
    private static final Logger logger = LocalLogger.getLogger();


    public JSON_Grid_Deserializer(){
        deserialize();
    }

    public void deserialize() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(ConstantsParser.JSON_GRID_FILE_LOCATION)) {

            JsonReader jsonReader = new JsonReader(reader);
            List<JSON_Array_Group> groups = gson.fromJson(jsonReader, new TypeToken<List<JSON_Array_Group>>(){}.getType());
            for(JSON_Array_Group group : groups) {
               switch (group.getName()) {
                   case ConstantsParser.CABLE_LIST_NAME:
                       cableGroup = group;
                       break;
                   case ConstantsParser.EP_LIST_NAME:
                       energyProducerGroup = group;
                       break;
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warning("There was a problem in the deserialization of the grid");
        }
    }


    private List<Cable> deserializeCables(){
        Gson gson = new Gson();
        List<Cable> cables;
        cables = gson.fromJson(cableGroup.getGroup().toString(), new TypeToken<List<Cable>>() {}.getType());
        return cables;
    }


    private List<EnergyProducer> deserializeEP(String agentName){
        Gson gson = new Gson();
        List<EnergyProducer> energyProducers = new ArrayList<>();

        List<JSON_Array_Group> agentGroups = gson.fromJson(energyProducerGroup.getGroup().toString(), new TypeToken<List<JSON_Array_Group>>() {}.getType());
        for(JSON_Array_Group agentGroup : agentGroups){
            if(!agentGroup.getName().equals(agentName)) continue;
            List<JSON_Array_Group> energyGroups = gson.fromJson(agentGroup.getGroup().toString(), new TypeToken<List<JSON_Array_Group>>() {}.getType());
            for(JSON_Array_Group energyGroup : energyGroups) {
                switch (energyGroup.getName()){
                    case ConstantsParser.SOLARPANEL_LIST_NAME:
                        List<SolarPanel> solarPanels = gson.fromJson(energyGroup.getGroup().toString(), new TypeToken<List<SolarPanel>>() {}.getType());
                        energyProducers.addAll(solarPanels);
                        break;
                    case ConstantsParser.WINMILL_LIST_NAME:
                        List<WindMill> windMills = gson.fromJson(energyGroup.getGroup().toString(), new TypeToken<List<WindMill>>() {}.getType());
                        energyProducers.addAll(windMills);
                        break;
                }
            }
        }
        return energyProducers;
    }

    public List<Cable> getCables(){
        return deserializeCables();
    }

    public List<EnergyProducer> getEnergyProducers(String agentName){
        return deserializeEP(agentName);
    }
}
