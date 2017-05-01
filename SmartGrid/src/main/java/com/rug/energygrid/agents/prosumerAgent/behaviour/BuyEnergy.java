package com.rug.energygrid.agents.prosumerAgent.behaviour;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class BuyEnergy {
    private List<Seller> sellers = new ArrayList<>(); // The agent who provides the best offer
    private double neededEnergy;
    private Agent parent;

    public BuyEnergy(Agent parent, double neededEnergy) {
        this.neededEnergy = neededEnergy;
        this.parent = parent;
    }

    public void divideEnergy() {
        for (Seller curBuyer : sellers) {
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
        for (Seller curSeller : sellers) {
            if (curSeller.getAgent().equals(agent)) {
                curSeller.sellingEnergy += sellingEnergy;
                return;
            }
        }
        sellers.add(new Seller(agent, neededEnergy));
    }

    private synchronized void updateBuyerList(Seller seller, double energyToBeBought) {
        Seller remainSeller = seller.remaining(energyToBeBought);
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

    private class Seller {
        private AID agent;
        private double sellingEnergy;

        public Seller(AID agent, double sellingEnergy) {
            this.agent = agent;
            this.sellingEnergy = sellingEnergy;
        }

        //Calculates what is the max energy that can be sold to this buyer
        public double calcEnergyToBeSold(double neededEnergy) {
            return sellingEnergy <= neededEnergy ? sellingEnergy : neededEnergy;
        }

        public Seller remaining(double energyToBeBought) {
            if (sellingEnergy > energyToBeBought) {
                return new Seller(agent, sellingEnergy-energyToBeBought);
            }
            return null;
        }

        public AID getAgent() {
            return agent;
        }
    }
}
