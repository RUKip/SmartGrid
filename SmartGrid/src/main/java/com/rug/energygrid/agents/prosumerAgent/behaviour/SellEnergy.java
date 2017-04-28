package com.rug.energygrid.agents.prosumerAgent.behaviour;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class SellEnergy {
    private List<Buyer> buyers = new ArrayList<>(); // The agent who provides the best offer
    private double surplusEnergy;
    private Agent parent;

    public SellEnergy(Agent parent, double surplusEnergy) {
        this.surplusEnergy = surplusEnergy;
        this.parent = parent;
    }

    public void divideEnergy() {
        for (Buyer curBuyer : buyers) {
            if (surplusEnergy > 0) {
                double energyToBeSold = curBuyer.calcEnergyToBeSold(surplusEnergy);
                subtractSurplusEnergy(energyToBeSold);
                parent.addBehaviour(new IndividualSellEnergyBehaviour(parent, curBuyer.getAgent(), energyToBeSold, this));
                updateBuyerList(curBuyer, energyToBeSold);
            } else {
                break;
            }
        }
    }

    public synchronized void addBuyer(AID agent, double neededEnergy) {
        for (Buyer curBuyer : buyers) {
            if (curBuyer.getAgent().equals(agent)) {
                curBuyer.neededEnergy += neededEnergy;
                return;
            }
        }
        buyers.add(new Buyer(agent, neededEnergy));
    }

    private synchronized void updateBuyerList(Buyer buyer, double energyToBeSold) {
        Buyer remainBuyer = buyer.remaining(energyToBeSold);
        if (remainBuyer != null) {
            buyers.add(remainBuyer);
        }
    }

    public double getSurplusEnergy() {
        return surplusEnergy;
    }

    public void subtractSurplusEnergy(double amount) {
        surplusEnergy -= amount;
    }

    public void addSurplusEnergy(double amount) {
        surplusEnergy += amount;
    }

    private class Buyer {
        private AID agent;
        private double neededEnergy;

        public Buyer(AID agent, double neededEnergy) {
            this.agent = agent;
            this.neededEnergy = neededEnergy;
        }

        //Calculates what is the max energy that can be sold to this buyer
        public double calcEnergyToBeSold(double surplusEnergy) {
            return neededEnergy <= surplusEnergy ? neededEnergy : surplusEnergy;
        }

        public Buyer remaining(double energyToBeSold) {
            if (neededEnergy > energyToBeSold) {
                return new Buyer(agent, neededEnergy-energyToBeSold);
            }
            return null;
        }

        public AID getAgent() {
            return agent;
        }
    }
}
