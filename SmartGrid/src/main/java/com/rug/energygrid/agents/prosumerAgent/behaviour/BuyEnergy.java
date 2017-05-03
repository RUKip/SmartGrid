package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.CustomPriorityQueue;
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
    private List<EnergyOffer> sellers = new ArrayList<>(); // The agent who provides the best offer //TODO: will be removed
    private CustomPriorityQueue pq;
    private ProsumerAgent prosumerAgent;

    //TODO: add the new behaviours
    public BuyEnergy(ProsumerAgent prosumerAgent) {
        this.pq = new CustomPriorityQueue(new GreedyComp()); //TODO: add a nice place to set/choose the Comperator
        this.prosumerAgent = prosumerAgent;
        refillEnergy();
    }

    public void refillEnergy() {
        while (prosumerAgent.getCurEnergy() < 0) {
            if (!pq.isEmpty()) {
                EnergyOffer energyOffer = pq.pop(prosumerAgent.getCurEnergy() * -1);
                double energyToBeBought = energyOffer.calcEnergyToBeBought(prosumerAgent.getCurEnergy() * -1);
                prosumerAgent.addCurEnergy(energyToBeBought);
                prosumerAgent.addBehaviour(new TransactionHandlerBuyer(prosumerAgent, this, energyToBeBought, energyOffer));
            } else {
                //TODO: add buying energy from the 'big guys'
                System.out.println("I have to buy energy at the big guys");
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

    //A bought behaviour couldn't buy all the energy that was planned.
    public void boughtLessEnergy(double energy) {
        prosumerAgent.subtractCurEnergy(energy);
        refillEnergy();
    }
}
