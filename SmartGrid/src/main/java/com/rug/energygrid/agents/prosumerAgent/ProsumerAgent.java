package com.rug.energygrid.agents.prosumerAgent;

import com.rug.energygrid.FinishedChecker;
import com.rug.energygrid.gatherData.GatherData;
import com.rug.energygrid.parser.JSON_Grid_Deserializer;
import com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm.ShortestPath;
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
import jade.domain.FIPAAgentManagement.ServiceDescription;
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
    private GatherData gatherData = GatherData.GATHER_DATA;
    protected double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    private double moneyBalance = 0; //The amount of money the prosumer currently has (can be negative)
    protected BuyEnergy buyEnergy;
    protected SellEnergy sellEnergy;
    private HashMap<String, Double> routingTable;  //KEY is ZIPCODE_HOUSENUMBER, TODO: check if this is unique as identifier
    private List<Cable> allCables;
    private List<EnergyProducer> energyProducers; //TODO: change To maxEnergy producers
    //TODO: add Adjustable Energy Producer list
    private List<EnergyConsumer> energyConsumers;
    private Weather usedWeather = new ExampleDataSet_KNI();
    private ServiceDescription sd; //The serviceDescription of an Agent

    @Override
    protected void setup() {
        super.setup();
        FinishedChecker.agentAdded();
        //logger = LocalLogger.getLogger();
        logger.info("name: "+getAID().getName()+" is alive!");
        buyEnergy = new BuyEnergy(this);
        sellEnergy = new SellEnergy(this);
        parseJSON(); //TODO: if no parser file exists then run initializer

        energyConsumers = new ArrayList<>(); //TODO: add this to parser
        energyConsumers.add(new GeneralEnergyConsumer());
        addToYellowPages();
    }

    @Override
    public void timedEvent(Instant end, Duration passedTime) {
        double newEnergy = 0;
        for (EnergyProducer ep : energyProducers) {
            try{
                newEnergy += ep.generateMaxEnergy(end, passedTime);
            }catch (NullPointerException e){
                e.printStackTrace();
                logger.warning("The weather data was Null, probably one of the elements in your data set is missing");
            }
        }
        for (EnergyConsumer ec : energyConsumers) {
            newEnergy -= ec.consumeEnergy(end, passedTime);
        }

        gatherData.addProduction(this.getAID(), end, newEnergy);
        addCurEnergy(newEnergy);
        //System.out.println("agent: "+this.getAID().getName()+" produced: "+newEnergy+" curEnergy: "+curEnergy);
        //logger.info("agent: "+this.getAID().getName()+" produced: "+newEnergy+" curEnergy: "+curEnergy);
    }

    //This method is ran when the agent shuts down
    @Override
    public void takeDown() {
        buyEnergy.takeDown();
        sellEnergy.takeDown();
        removeService(sd);
        FinishedChecker.agentRemoved();
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
        addCurEnergy(energy*-1);
    }

    public void addMoney(double amount) {
        moneyBalance += amount;
    }

    public void subtractMoney(double amount) {
        moneyBalance -= amount;
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

    private void parseJSON(){
        JSON_Grid_Deserializer deserializer = new JSON_Grid_Deserializer();
        allCables = deserializer.getCables();
        energyProducers = deserializer.getEnergyProducers(this.getLocalName());
        for(EnergyProducer e : energyProducers){
            e.setWeather(usedWeather);
        }
        routingTable = new ShortestPath().calcShortestPath(this.getLocalName(), allCables);
    }

    public double getRoutingValueTo(String agent){
        return this.routingTable.get(agent);
    }

    private void addToYellowPages() {
        // Register the agent as a Timed instance
        sd = new ServiceDescription();
        sd.setType(ProsumerConstants.PROSUMER_SD);
        sd.setName("prosumerAgent");
        addService(sd);
    }
}
