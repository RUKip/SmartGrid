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
    private List<EnergyProducer> energyProducerList;
    //private List<List<EnergyConsumer>> energyConsumerList; TODO: implement if we are going to use it
    private List<Cable> cableList;

    public Initializer(){
      this.energyProducerList = new ArrayList();
      this.cableList = new ArrayList();
      this.weather = new ExampleDataSet_KNI();
      initAllCables();
      initEnergyProducers();

    }

    private void initAllCables(){
        cableList.add(new Cable("1111HJ60b", "8748NJ373", 42, 0.3));
        cableList.add(new Cable("9733AB50", "1111HJ60b", 27, 0.7));
        cableList.add(new Cable("9717KH6", "9733AB50", 30, 0.2));
        cableList.add(new Cable("9471KN24", "9717KH6", 8, 0.2));
    }

    private void initEnergyProducers(){
        energyProducerList.add(new SolarPanel(weather));
        energyProducerList.add(new SolarPanel(weather));
        energyProducerList.add(new WindMill(weather));
    }

    public void build(){
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("D:\\grid.json")) {
            g.toJson(new JSON_Array_Group("Cables", cableList), writer);
            //writer.write('\n');
            g.toJson(new JSON_Array_Group("EnergyProducers",energyProducerList), writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
