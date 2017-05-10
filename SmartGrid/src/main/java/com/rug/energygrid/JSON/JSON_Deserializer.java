package com.rug.energygrid.JSON;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;
import com.rug.energygrid.energyProducers.SolarPanel;
import com.rug.energygrid.energyProducers.WindMill;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 09-May-17.
 */
public class JSON_Deserializer {

    private List<Cable> cables;
    private List<EnergyProducer> energyProducers = new ArrayList<>();

    public JSON_Deserializer(){
        deserialize();
    }

    private void deserialize() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(ConstantsJSON.JSON_FILE_LOCATION)) {

            JsonReader jsonReader = new JsonReader(reader);
            List<JSON_Array_Group> groups = gson.fromJson(jsonReader, new TypeToken<List<JSON_Array_Group>>(){}.getType());
            for(JSON_Array_Group group : groups) {
               switch (group.getName()) {
                   case ConstantsJSON.CABLE_LIST_NAME:
                       cables = gson.fromJson(group.getGroup().toString(), new TypeToken<List<Cable>>() {}.getType()); //TODO: check this another time i think im kinda doing something hacky here
                       break;
                   case ConstantsJSON.EP_LIST_NAME:
                       deserializeEP(group);
                       break;
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserializeEP(JSON_Array_Group group){
        Gson gson = new Gson();
        System.out.println(group.getGroup().toString());
        List<JSON_Array_Group> agentGroups = gson.fromJson(group.getGroup().toString(), new TypeToken<List<JSON_Array_Group>>() {}.getType());
        for(JSON_Array_Group agentGroup : agentGroups){
            System.out.println("    " + agentGroup.getName());
            System.out.println("    " + agentGroup.getGroup().toString());
            List<JSON_Array_Group> energyGroups = gson.fromJson(agentGroup.getGroup().toString(), new TypeToken<List<JSON_Array_Group>>() {}.getType());
            for(JSON_Array_Group energyGroup : energyGroups) {
                System.out.println("         " + energyGroup.getName());
                switch (energyGroup.getName()){
                    case ConstantsJSON.SOLARPANEL_LIST_NAME:
                        List<SolarPanel> solarPanels = gson.fromJson(group.getGroup().toString(), new TypeToken<List<SolarPanel>>() {}.getType());
                        energyProducers.addAll(solarPanels);
                        break;
                    case ConstantsJSON.WINMILL_LIST_NAME:
                        List<WindMill> windMills = gson.fromJson(group.getGroup().toString(), new TypeToken<List<WindMill>>() {}.getType());
                        energyProducers.addAll(windMills);
                        break;
                }
            }
        }
    }

    public List<Cable> getCables(){
        return this.cables;
    }

    public List<EnergyProducer> getEnergyProducers(){
        return this.energyProducers;
    }
}
