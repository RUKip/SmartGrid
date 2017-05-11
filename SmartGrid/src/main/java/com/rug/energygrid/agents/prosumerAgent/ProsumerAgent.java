package com.rug.energygrid.agents.prosumerAgent;

import com.rug.energygrid.JSON.JSON_Deserializer;
import com.rug.energygrid.agents.time.timedAgent.TimedAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.BuyEnergy;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellEnergy;
import com.rug.energygrid.energyProducers.EnergyProducer;
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
    private final Logger logger = jade.util.Logger.getMyLogger(this.getClass().getName());

    private double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    private double realEnergy = 0; //This is the real amount of energy (if for example energy is sold it will be subtracted from this.
    private BuyEnergy buyEnergy;
    private SellEnergy sellEnergy;
    private HashMap<String, Double> routingTable;  //KEY is ZIPCODE_HOUSENUMBER, TODO: check if this is unique as identifier
    private List<Cable> allCables;
    private List<EnergyProducer> energyProducers;

    @Override
    protected void setup() {
        super.setup();
        final double startEnergy = Double.parseDouble((String) this.getArguments()[0]);
        System.out.println("name: "+getAID().getName()+" energy: "+ startEnergy);
        curEnergy = startEnergy;
        buyEnergy = new BuyEnergy(this);
        sellEnergy = new SellEnergy(this);
        parseJSON();
        logger.log(Logger.SEVERE, "heftuuuuug: "+ this.getAID().getLocalName());
    }

    //Used when a behaviour sold or bought energy, the real energy left in the system has to be updated.
    public void updateRealEnergy(double energy) {
        //Is negated since if you sell energy the realenergy goes down.
        realEnergy -= energy;
    }

    @Override
    public void timedEvent(Instant end, Duration passedTime) {
        //TODO: do stuff add timed events.
    }

    @Override
    public void takeDown() {
        buyEnergy.takeDown();
        sellEnergy.takeDown();
    }

    public synchronized void addCurEnergy(double energy) {
        curEnergy += energy;
    }

    public synchronized void subtractCurEnergy(double energy) {
        curEnergy -= energy;
        buyEnergy.refillEnergy();
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
    }
}
