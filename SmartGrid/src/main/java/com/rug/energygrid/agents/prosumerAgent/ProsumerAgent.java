package com.rug.energygrid.agents.prosumerAgent;

import com.rug.energygrid.JSON.JSON_Deserializer;
import com.rug.energygrid.agents.time.StaticTest;
import com.rug.energygrid.agents.time.timedAgent.TimedAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.BuyEnergy;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellEnergy;
import com.rug.energygrid.energyConsumers.EnergyConsumer;
import com.rug.energygrid.energyConsumers.GeneralEnergyConsumer;
import com.rug.energygrid.energyProducers.EnergyProducer;
import com.rug.energygrid.logging.LocalLogger;
import com.rug.energygrid.weather.ExampleDataSet_KNI;
import com.rug.energygrid.weather.Weather;
import jade.util.Logger;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class ProsumerAgent extends TimedAgent {
    private static final Logger logger = LocalLogger.getLogger();
    private double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    private double realEnergy = 0; //This is the real amount of energy (if for example energy is sold it will be subtracted from this.
    private BuyEnergy buyEnergy;
    private SellEnergy sellEnergy;
    private HashMap<String, Double> routingTable;  //KEY is ZIPCODE_HOUSENUMBER, TODO: check if this is unique as identifier
    private List<Cable> allCables;
    private List<EnergyProducer> energyProducers; //TODO: change To maxEnergy producers
    //TODO: add Adjustable Energy Producer list
    private List<EnergyConsumer> energyConsumers;
    private Weather usedWeather = new ExampleDataSet_KNI();

    @Override
    protected void setup() {
        super.setup();
        //logger = LocalLogger.getLogger();
        logger.info("name: "+getAID().getName()+" is alive!"); //TODO: make log statement
        buyEnergy = new BuyEnergy(this);
        sellEnergy = new SellEnergy(this);
        parseJSON(); //TODO: if no JSON file exists then run initializer

        energyConsumers = new ArrayList<>(); //TODO: add this to JSON
        energyConsumers.add(new GeneralEnergyConsumer());
    }

    //Used when a behaviour sold or bought energy, the real energy left in the system has to be updated.
    public void updateRealEnergy(double energy) {
        //Is negated since if you sell energy the realenergy goes down.
        realEnergy -= energy;
    }

    @Override
    public void timedEvent(Instant end, Duration passedTime) {
        double newEnergy = 0;
        for (EnergyProducer ep : energyProducers) {
            newEnergy += ep.generateMaxEnergy(end, passedTime);
        }

        for (EnergyConsumer ec : energyConsumers) {
            newEnergy -= ec.consumeEnergy(end, passedTime);
        }

        addCurEnergy(newEnergy);
        System.out.println("agent: "+this.getAID().getName()+" produced: "+newEnergy+" curEnergy: "+curEnergy);
        //logger.info("agent: "+this.getAID().getName()+" produced: "+newEnergy+" curEnergy: "+curEnergy);
    }

    @Override
    public void takeDown() {
        buyEnergy.takeDown();
        sellEnergy.takeDown();
    }

    public synchronized void addCurEnergy(double energy) {
        curEnergy += energy;
        if (curEnergy > 0) {
            sellEnergy.sellSurplussEnergy();
        } else {
            buyEnergy.refillEnergy();
        }
    }

    public synchronized void subtractCurEnergy(double energy) {
        curEnergy -= energy;
        if (curEnergy > 0) {
            sellEnergy.sellSurplussEnergy();
        } else {
            buyEnergy.refillEnergy();
        }
    }

    public synchronized double getCurEnergy() {
        return curEnergy;
    }

    //Can be used to extend the grid with an extra node later on.
    public synchronized void addNode(String node, Cable ... neighbours){
        double shortestPath = Double.MAX_VALUE;
        for(Cable neighbour : neighbours) {
            Double neigbourCost = routingTable.get(neighbour.getConnectedNode());
            if (neigbourCost != null) {
                neigbourCost += neighbour.getCost();
                if (neigbourCost < shortestPath) shortestPath = neigbourCost;
            }
        }
        if(shortestPath<Double.MAX_VALUE) routingTable.put(node, shortestPath);
    }

    //TODO: parse JSON value of costs and nodes then implement dijkstra search algorithm to put right values into routingTable
    private void parseJSON(){
        JSON_Deserializer deserializer = new JSON_Deserializer();
        allCables = deserializer.getCables();
        energyProducers = deserializer.getEnergyProducers(this.getLocalName());
        for(EnergyProducer e : energyProducers){
            e.setWeather(usedWeather);
        }
        //routingTable = new ShortestPath().calcShortestPath(this.getLocalName(), allCables); TODO: commented out since it gave errors.
    }
}
