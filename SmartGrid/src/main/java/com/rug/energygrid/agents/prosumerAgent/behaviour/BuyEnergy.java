package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.EnergyOffer;
import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class BuyEnergy {
    private List<EnergyOffer> sellers = new ArrayList<>(); // The agent who provides the best offer
    private double neededEnergy;
    private Agent parent;

    public BuyEnergy(Agent parent, double neededEnergy) {
        this.neededEnergy = neededEnergy;
        this.parent = parent;
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
        for (EnergyOffer curSeller : sellers) {
            if (curSeller.getAgent().equals(agent)) {
                curSeller.sellingEnergy += sellingEnergy;
                return;
            }
        }
        sellers.add(new EnergyOffer(agent, neededEnergy));
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
