package com.rug.energygrid.agents.prosumerAgent;

import com.google.gson.Gson;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.BuyEnergy;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellEnergy;
import jade.core.Agent;


import java.util.HashMap;

/**
 * Created by thijs on 28-4-17.
 */
public class ProsumerAgent extends Agent {
    private double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    private double realEnergy = 0; //This is the real amount of energy (if for example energy is sold it will be subtracted from this.
    private BuyEnergy buyEnergy;
    private SellEnergy sellEnergy;
    private HashMap<String, Double> routingTable;  //KEY is ZIPCODE_HOUSENUMBER, TODO: check if this is unique as identifier

    @Override
    protected void setup() {
        final double startEnergy = Double.parseDouble((String) this.getArguments()[0]);
        System.out.println("name: "+getAID().getName()+" energy: "+ startEnergy);
        curEnergy = startEnergy;
        buyEnergy = new BuyEnergy(this);
        sellEnergy = new SellEnergy(this);
        parseJSON();
    }

    //Used when a behaviour sold or bought energy, the real energy left in the system has to be updated.
    public void updateRealEnergy(double energy) {
        //Is negated since if you sell energy the realenergy goes down.
        realEnergy -= energy;
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
        Gson g = new Gson();
        //g.fromJson()
    }
}
