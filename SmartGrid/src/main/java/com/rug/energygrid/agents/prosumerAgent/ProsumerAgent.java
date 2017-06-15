package com.rug.energygrid.agents.prosumerAgent;

import com.rug.energygrid.FinishedChecker;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellingAgent;
import com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm.GraphTuple;
import com.rug.energygrid.gatherData.GatherData;
import com.rug.energygrid.parser.JSON_Grid_Deserializer;
import com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm.ShortestPath;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.BuyEnergy;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
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
import java.util.*;

public class ProsumerAgent extends SellingAgent {
    private static final Logger logger = LocalLogger.getLogger();
    private GatherData gatherData = GatherData.GATHER_DATA;
    protected BuyEnergy buyEnergy;
    private HashMap<String, Double> routingTable, lengthTable;  //KEY is ZIPCODE_HOUSENUMBER, has to be unique!!!
    private List<Cable> allCables;
    private List<EnergyProducer> energyProducers;
    //TODO: add Adjustable Energy Producer list, (generators etc)
    private List<EnergyConsumer> energyConsumers;
    private Weather usedWeather = new ExampleDataSet_KNI(); //TODO: should be got from parser (Parse Json)
    private ServiceDescription sd; //The serviceDescription of an Agent
    private Queue<GenEntry> generationQueue = new ArrayDeque<>();

    @Override
    protected void setup() {
        super.setup();
        FinishedChecker.agentAdded();
        buyEnergy = new BuyEnergy(this);
        parseJSON(); //TODO: if no parser file exists then run initializer
        energyConsumers = new ArrayList<>(); //TODO: add this to parser
        energyConsumers.add(new GeneralEnergyConsumer());
        addToYellowPages();
        logger.info("NAME: "+getAID().getName()+" is alive!");
    }

    private double generatedEnergy(Instant end, Duration passedTime) {
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
        return newEnergy;
    }

    @Override
    public void timedEvent(Instant end, Duration passedTime) {
        double newEnergy = generatedEnergy(end, passedTime);
        gatherData.addProduction(this.getAID(), end, newEnergy);
        addCurEnergy(newEnergy);
        //checkGenTable(end);
        sellEnergy.sellSurplussEnergy();
        gatherData.addEnergyStatus(this.getAID(), end, curEnergy);
    }

    //This method is ran when the agent shuts down
    @Override
    public void takeDown() {
        removeService(sd);
        buyEnergy.takeDown();
        super.takeDown();
        FinishedChecker.agentRemoved();
    }

    public synchronized void addCurEnergy(double energy) {
        updateGenTable(energy);
        addCurEnergyWithoutTable(energy);
    }

    public synchronized void subtractCurEnergy(double energy) {
        addCurEnergy(energy*-1);
    }

    private void addCurEnergyWithoutTable(double energy) {
        curEnergy += energy;
        if (curEnergy > 0) {
            //This caused the seller to overflow all other agents with sell messages.
            //sellEnergy.sellSurplussEnergy();
        } else if (curEnergy < 0){
            buyEnergy.refillEnergy();
        }
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

    public void parseJSON(){
        JSON_Grid_Deserializer deserializer = new JSON_Grid_Deserializer();
        allCables = deserializer.getCables();
        energyProducers = deserializer.getEnergyProducers(this.getLocalName());
        for(EnergyProducer e : energyProducers){
            e.setWeather(usedWeather);
        }
        GraphTuple graphs = new ShortestPath().calcShortestPath(this.getLocalName(), allCables);

        routingTable = graphs.getFinalGraph();
        lengthTable = graphs.getLengthGraph();
    }

    public double getRoutingValueTo(String agent){
        return this.routingTable.get(agent);
    }
    public double getRoutingLengthTo(String agent){
        return this.lengthTable.get(agent);
    }


    private void addToYellowPages() {
        // Register the agent as a Timed instance
        sd = new ServiceDescription();
        sd.setType(ProsumerConstants.PROSUMER_SD);
        sd.setName("prosumerAgent");
        addService(sd);
    }

    private void checkGenTable(Instant end) {
        Instant deadline = end.minus(ProsumerConstants.ENERGY_MAX_KEEP_TIME);
        double energyChange = 0;
        while (!generationQueue.isEmpty() && generationQueue.peek().genTime.isAfter(deadline)) {
            GenEntry curEntry = generationQueue.remove();
            energyChange += curEntry.energy;
        }
        if (energyChange > 0) {
            addCurEnergyWithoutTable(energyChange);
            //addBehaviour(new SellToBigGuyBhvr(this, energyChange));
        }
    }

    private void updateGenTable(double energy) {
        // Add to the gen table if it was a positive production.
        double curEnergy = this.getCurEnergy();
        if (energy > 0 && curEnergy+energy > 0) {
            generationQueue.add(new GenEntry(curTime, Math.min(energy,curEnergy+energy)));
            return;
        }
        if (energy < 0) {
            // energy is here negative since it is consuming.
            while (!generationQueue.isEmpty() && generationQueue.peek().energy <= energy * -1) {
                energy += generationQueue.remove().energy;
            }
            if (!generationQueue.isEmpty()) {
                generationQueue.peek().energy += energy;
            }
        }
    }

    private class GenEntry {
        public Instant genTime;
        public double energy;

        public GenEntry(Instant genTime, double energy) {
            this.genTime = genTime;
            this.energy = energy;
        }
    }
}
