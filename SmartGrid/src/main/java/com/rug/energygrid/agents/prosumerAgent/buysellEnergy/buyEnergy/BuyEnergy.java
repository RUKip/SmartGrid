package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms.GreedyEnergy;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class BuyEnergy {
    private List<RemoteEnergyOffer> sellers = new ArrayList<>(); // The agent who provides the best offer //TODO: will be removed
    private CustomPriorityQueue pq;
    private ProsumerAgent prosumerAgent;
    private ServiceDescription sd;
    private MessageHandlerBuyerBehaviour messageHandler;

    //TODO: add the new behaviours
    public BuyEnergy(ProsumerAgent prosumerAgent) {
        this.pq = new CustomPriorityQueue(new GreedyEnergy()); //TODO: add a nice place to set/choose the Comperator
        this.prosumerAgent = prosumerAgent;
        messageHandler = new MessageHandlerBuyerBehaviour(prosumerAgent, this);
        prosumerAgent.addBehaviour(messageHandler);
        registerConsumer();
        refillEnergy();
    }

    private void registerConsumer() {
        // Register the agent as a consumer
        sd = new ServiceDescription();
        sd.setType(BuySellComConstants.CONSUMER_SD);
        sd.setName("Buyers");
        prosumerAgent.addService(sd);
    }

    public void refillEnergy() {
        while (prosumerAgent.getCurEnergy() < 0) {
            System.out.println("amount of message: "+pq.getSize());
            if (!pq.isEmpty()) {
                RemoteEnergyOffer energyOffer = pq.pop(prosumerAgent.getCurEnergy() * -1);
                double energyToBeBought = energyOffer.calcEnergyToBeBought(prosumerAgent.getCurEnergy() * -1);
                prosumerAgent.addCurEnergy(energyToBeBought);
                prosumerAgent.addBehaviour(new TransactionHandlerBuyer(prosumerAgent, this, energyToBeBought, energyOffer));
            } else {
                //TODO: add buying energy from the 'big guys'
                //System.out.println("I have to buy energy at the big guys");
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
        prosumerAgent.removeService(sd);
    }
}
