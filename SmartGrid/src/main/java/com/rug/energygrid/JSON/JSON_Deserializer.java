package com.rug.energygrid.JSON;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ruben on 09-May-17.
 */
public class JSON_Deserializer {

    private List<Cable> cables;
    private List<EnergyProducer> energyProducers;

    public JSON_Deserializer(){
        deserialize();
    }

    private void deserialize() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(ConstantsJSON.JSON_FILE_LOCATION)) {

            JsonReader jsonReader = new JsonReader(reader);
            List<JSON_Array_Group> groups = gson.fromJson(jsonReader, new TypeToken<List<JSON_Array_Group>>(){}.getType());
            for(JSON_Array_Group group : groups) {
//                System.out.println(group.getName());
//                System.out.println(group.getGroup());
               switch (group.getName()){
                   case ConstantsJSON.CABLE_LIST_NAME:
                       cables = gson.fromJson(group.getGroup().toString(),  new TypeToken<List<Cable>>(){}.getType()); //TODO: check this another time i think im kinda doing something hacky here
                       break;
                   case ConstantsJSON.EP_LIST_NAME:
//                       energyProducers = gson.fromJson(group.getGroup().toString(),  new TypeToken<List<EnergyProducer>>(){}.getType());; //TODO: fix this with dubble nested list
                       break;
               }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Cable> getCables(){
        return this.cables;
    }

    public List<EnergyProducer> getEnergyProducers(){
        return this.energyProducers;
    }
}
