package com.rug.energygrid.JSON;

import com.google.gson.Gson;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 08-May-17.
 */
public class Initializer {

    private List<List<EnergyProducer>> energyProducerList;
    //private List<List<EnergyConsumer>> energyConsumerList; TODO: implement if we are going to use it
    private List<Cable> cableList;

    public Initializer(){
      this.energyProducerList = new ArrayList();
      this.cableList = new ArrayList();

      initAllCables();

    }

    private void initAllCables(){
        cableList.add(new Cable());
    }

    public void build(){
        Gson g = new Gson();
        String json = g.toJson(cableList);
        System.out.println(json);

        try (FileWriter writer = new FileWriter("D:\\grid.json")) {
            g.toJson(cableList, writer);
            g.toJson(energyProducerList, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
