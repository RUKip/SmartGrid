package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by thijs on 2-5-17.
 */
public class MessageHandlerBuyerBehaviour extends CyclicBehaviour {
    private MessageTemplate mt = MessageTemplate.MatchConversationId(BuySellComConstants.ENERGY_OFFER_MESSAGE);
    private BuyEnergy buyEnergy;
    private ServiceDescription sd;

    public MessageHandlerBuyerBehaviour(Agent myAgent, BuyEnergy buyEnergy) {
        super(myAgent);
        this.buyEnergy = buyEnergy;
    }

    @Override
    public void action() {
        ACLMessage reply = myAgent.receive(mt);
        if (reply != null) {
            // Reply received
            System.out.println("got an RemoteEnergyOffer: "+reply.getContent());
            if (reply.getPerformative() == ACLMessage.PROPOSE) {
                // This is an offer
                EnergyOffer energyOffer = EnergyOffer.deserialize(reply.getContent());

                //Store the RemoteEnergyOffer in the priorityQueue
                buyEnergy.addEnergyOffer(new RemoteEnergyOffer(reply.getSender(), energyOffer));
            }
        }
        else {
            block();
        }
    }
}
