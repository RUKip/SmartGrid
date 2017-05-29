package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.bigGuy.BigGuyAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms.GreedyPrice;
import com.rug.energygrid.gatherData.GatherData;
import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms.GreedyEnergy;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 28-4-17.
 */
public class BuyEnergy {
    private GatherData gatherData = GatherData.GATHER_DATA;

    private List<RemoteEnergyOffer> sellers = new ArrayList<>(); // The agent who provides the best offer //TODO: will be removed
    private CustomPriorityQueue pq;
    private ProsumerAgent prosumerAgent;
    private ServiceDescription sd;
    private MessageHandlerBuyerBehaviour messageHandler;

    //TODO: add the new behaviours
    public BuyEnergy(ProsumerAgent prosumerAgent) {
        this.pq = new CustomPriorityQueue(new GreedyPrice()); //TODO: add a nice place to set/choose the Comperator
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
            if (!pq.isEmpty()) {
                System.out.println("I am :"+ prosumerAgent.getAID().getLocalName());
                pq.print();
                RemoteEnergyOffer energyOffer = pq.pop();
                double neededEnergy = prosumerAgent.getCurEnergy() * -1;
                RemoteEnergyOffer.MaxAndRemaining maxAndRemaining = energyOffer.getMaxAndRemaining(neededEnergy);
                RemoteEnergyOffer max = maxAndRemaining.max;
                pq.add(maxAndRemaining.remaining);

                System.out.println("offer: " +energyOffer.getEnergy()+ "seller: "+energyOffer.getAgent().getLocalName()+ " need: "+neededEnergy+ " bought: "+ max.getEnergy()+" to be gained: "+ max.getEnergyLeft());

                prosumerAgent.addCurEnergy(max.getEnergyLeft()); //TODO: shouldnt this needed energy? (and energytobebought should be subtracted from seller)
                prosumerAgent.addBehaviour(new TransactionHandlerBuyer(prosumerAgent, this, max));
            } else {
                /*//TODO: add buying energy from the 'big guys' with a real agent
                double energy = prosumerAgent.getCurEnergy()*-1;
                prosumerAgent.addCurEnergy(energy);
                processPayment(null, BigGuyAgent.CONSTANTPRICE, energy);*/
                break;
            }
        }
    }

    //Only the buyer stores the payments also in the GatherData
    public void processPayment(AID seller, double price, double amountEnergy) {
        prosumerAgent.subtractMoney(price*amountEnergy);
        gatherData.addDeal(prosumerAgent.getCurrentTime(), seller, prosumerAgent.getAID(), price, amountEnergy);
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

    public ProsumerAgent getProsumerAgent(){
        return this.prosumerAgent;
    }
}
