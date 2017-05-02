package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.EnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.GreedyComp;
import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
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
    private ProsumerAgent myAgent;

    //TODO: add the new behaviours
    public BuyEnergy(ProsumerAgent myAgent, double neededEnergy) {
        this.neededEnergy = neededEnergy;
        this.myAgent = myAgent;
    }

    public void divideEnergy() {
        for (EnergyOffer curBuyer : sellers) {
            if (neededEnergy > 0) {
                double energyToBeBought = curBuyer.calcEnergyToBeSold(neededEnergy);
                subtractNeededEnergy(energyToBeBought);
                myAgent.addBehaviour(new IndividualBuyEnergyBehaviour(myAgent, curBuyer.getAgent(), energyToBeBought, this));
                updateBuyerList(curBuyer, energyToBeBought);
            } else {
                break;
            }
        }
    }

    public synchronized void addSeller(AID agent, double sellingEnergy) {

        //TODO: priority queue
        sellers.add(new EnergyOffer(agent, sellingEnergy, new GreedyComp()));
    }

    private synchronized void updateBuyerList(EnergyOffer seller, double energyToBeBought) {
        EnergyOffer remainSeller = seller.remaining(energyToBeBought);
        if (remainSeller != null) {
            sellers.add(remainSeller);
        }
    }

    //A bought behaviour couldn't buy all the energy that was planned.
    public void boughtLessEnergy(double energy) {
        myAgent.subtractCurEnergy(energy);
        divideEnergy();
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
