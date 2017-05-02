package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.CustomPriorityQueue;
import com.rug.energygrid.agents.prosumerAgent.EnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.GreedyComp;
import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class BuyEnergy {
    private List<EnergyOffer> sellers = new ArrayList<>(); // The agent who provides the best offer //TODO: will be removed
    private double neededEnergy;
    private Agent parent;
    private CustomPriorityQueue pq;

    public BuyEnergy(Agent parent, double neededEnergy) {
        this.neededEnergy = neededEnergy;
        this.parent = parent;
        this.pq = new CustomPriorityQueue(new GreedyComp()); //TODO: add a nice place to set/choose the Comperator
    }

    public void divideEnergy() {
        for (EnergyOffer curBuyer : sellers) {
            if (neededEnergy > 0) {
                double energyToBeBought = curBuyer.calcEnergyToBeSold(neededEnergy);
                subtractNeededEnergy(energyToBeBought);
                parent.addBehaviour(new IndividualBuyEnergyBehaviour(parent, curBuyer.getAgent(), energyToBeBought, this));
                updateBuyerList(curBuyer, energyToBeBought);
            } else {
                break;
            }
        }
    }

    public synchronized void addSeller(AID agent, double sellingEnergy) {
        pq.add(new EnergyOffer(agent, sellingEnergy, new GreedyComp()));
    }

    private synchronized void updateBuyerList(EnergyOffer seller, double energyToBeBought) {
        EnergyOffer remainSeller = seller.remaining(energyToBeBought);
        if (remainSeller != null) {
            sellers.add(remainSeller);
        }
    }

    public double getNeededEnergy() {
        return neededEnergy;
    }

    public void subtractNeededEnergy(double amount) {
        neededEnergy -= amount;
    }

    public void addNeededEnergy(double amount) {
        neededEnergy += amount;
    }
}
