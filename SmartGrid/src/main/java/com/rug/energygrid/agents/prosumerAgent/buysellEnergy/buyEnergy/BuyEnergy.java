package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms.GreedyComp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class BuyEnergy {
    private List<RemoteEnergyOffer> sellers = new ArrayList<>(); // The agent who provides the best offer //TODO: will be removed
    private CustomPriorityQueue pq;
    private ProsumerAgent prosumerAgent;
    private MessageHandlerBuyerBehaviour messageHandler;

    //TODO: add the new behaviours
    public BuyEnergy(ProsumerAgent prosumerAgent) {
        this.pq = new CustomPriorityQueue(new GreedyComp()); //TODO: add a nice place to set/choose the Comperator
        this.prosumerAgent = prosumerAgent;
        messageHandler = new MessageHandlerBuyerBehaviour(prosumerAgent, this);
        refillEnergy();
    }

    public void refillEnergy() {
        while (prosumerAgent.getCurEnergy() < 0) {
            if (!pq.isEmpty()) {
                RemoteEnergyOffer energyOffer = pq.pop(prosumerAgent.getCurEnergy() * -1);
                double energyToBeBought = energyOffer.calcEnergyToBeBought(prosumerAgent.getCurEnergy() * -1);
                prosumerAgent.addCurEnergy(energyToBeBought);
                prosumerAgent.addBehaviour(new TransactionHandlerBuyer(prosumerAgent, this, energyToBeBought, energyOffer));
            } else {
                //TODO: add buying energy from the 'big guys'
                System.out.println("I have to buy energy at the big guys");
                prosumerAgent.addCurEnergy(prosumerAgent.getCurEnergy()*-1);
            }
        }
    }

    public synchronized void addEnergyOffer(RemoteEnergyOffer energyOffer) {
        pq.add(energyOffer);
    }

    //A bought behaviour couldn't buy all the energy that was planned.
    public void boughtLessEnergy(double energy) {
        prosumerAgent.subtractCurEnergy(energy);
        refillEnergy();
    }

    //This method is called when the agent shuts down.
    public void takeDown() {
        messageHandler.takeDown();
    }
}
