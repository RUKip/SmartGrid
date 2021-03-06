package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.agents.prosumerAgent.ProsumerConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class SellEnergy {
    public static final int PERFECT_DEAL = 0, LESSTHAN_DEAL = 1, NO_DEAL = 2;
    private SellingAgent sellingAgent;
    private double localEnergyPrice;

    public SellEnergy(SellingAgent sellingAgent, double energyPrice) {
        this.localEnergyPrice = energyPrice;
        this.sellingAgent = sellingAgent;
        sellingAgent.addBehaviour(new MessageHandlerSellerBhvr(sellingAgent, this));
    }

    public int compareDeal(EnergyOffer energyOffer) {
        //There was no offer or no energy is left
        if (energyOffer.getPrice() != localEnergyPrice || sellingAgent.getCurEnergy() <= 0) {
            return NO_DEAL;
        }

        //There is an offer
        if (sellingAgent.getCurEnergy() >= energyOffer.getEnergy()) {
            return PERFECT_DEAL;
        } else {
            return LESSTHAN_DEAL;
        }
    }

    //reserves min(energyOffer, currentEnergy) at the agent and returns this value.
    public double reserveEnergy(double energy) {
        double soldEnergy = Math.min(energy, sellingAgent.getCurEnergy());
        sellingAgent.subtractCurEnergy(soldEnergy);
        return soldEnergy;
    }

    public void sellSurplussEnergy() {
        double energy = sellingAgent.getCurEnergy();
        if (energy > 0) {
            EnergyOffer offer = new EnergyOffer(localEnergyPrice, energy);
            broadCastOffer(offer);
        }
    }

    private void broadCastOffer(EnergyOffer offer) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(BuySellComConstants.CONSUMER_SD);
        template.addServices(sd);
        ACLMessage energyOffer = new ACLMessage(ACLMessage.PROPOSE);
        energyOffer.setConversationId(BuySellComConstants.ENERGY_OFFER_MESSAGE);
        energyOffer.setContent(offer.serialize());
        try {
            DFAgentDescription[] result = DFService.search(sellingAgent, template);
            for (int i = 0; i < result.length; ++i) {
                //To make sure that you don't send offers to yourself
                if (!result[i].getName().equals(sellingAgent.getAID())) {
                    energyOffer.addReceiver(result[i].getName());
                }
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        sellingAgent.send(energyOffer);
    }

    //This method is called when the agent shutsdown.
    public void takeDown() {
        //nothing needs to happen.
    }

    //updates the local selling price and notifies all buyers.
    public void setLocalEnergyPrice(double localEnergyPrice) {
        this.localEnergyPrice = localEnergyPrice;
        sellSurplussEnergy();
    }

    public void processPayment(double price, double amountEnergy) {
        sellingAgent.addMoney(price*amountEnergy);
    }
}
