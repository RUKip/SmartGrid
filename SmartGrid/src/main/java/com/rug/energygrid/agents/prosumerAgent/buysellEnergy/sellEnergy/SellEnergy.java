package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * Created by thijs on 2-5-17.
 */
public class SellEnergy {
    public static final int PERFECT_DEAL = 0, LESSTHAN_DEAL = 1, NO_DEAL = 2;
    ProsumerAgent prosumerAgent;

    public SellEnergy(ProsumerAgent prosumerAgent) {
        this.prosumerAgent = prosumerAgent;
        prosumerAgent.addBehaviour(new MessageHandlerSellerBehaviour(prosumerAgent, this));
    }

    public int compareDeal(double energyOffer) {
        //There was no offer or no energy is left
        //TODO: IMPLEMENTATION FOR OFFER CHECKING SHOULD BE ADDED AT false LATER
        if (false || prosumerAgent.getCurEnergy() <= 0) {
            return NO_DEAL;
        }

        //There is an offer
        if (prosumerAgent.getCurEnergy() >= energyOffer) {
            return PERFECT_DEAL;
        } else {
            return LESSTHAN_DEAL;
        }
    }

    //reserves min(energyOffer, currentEnergy) at the agent and returns this value.
    public double reserveEnergy(double energyOffer) {
        double soldEnergy = Math.min(prosumerAgent.getCurEnergy(), energyOffer);
        prosumerAgent.subtractCurEnergy(soldEnergy);
        return soldEnergy;
    }

    public void sellSurplussEnergy() {
        double energy = prosumerAgent.getCurEnergy();
        if (energy > 0) {
            EnergyOffer offer = new EnergyOffer(energy);
            broadCastOffer(offer);
        }
    }

    public void broadCastOffer(EnergyOffer offer) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(BuySellComConstants.CONSUMER_SD);
        template.addServices(sd);

        ACLMessage energyOffer = new ACLMessage(ACLMessage.PROPOSE);
        energyOffer.setConversationId(BuySellComConstants.ENERGY_OFFER_MESSAGE);
        energyOffer.setContent(offer.serialize());
        try {
            DFAgentDescription[] result = DFService.search(prosumerAgent, template);
            for (int i = 0; i < result.length; ++i) {
                energyOffer.addReceiver(result[i].getName());
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        prosumerAgent.send(energyOffer);
    }

    //This method is called when the agent shutsdown.
    public void takeDown() {
        //nothing needs to happen.
    }
}
