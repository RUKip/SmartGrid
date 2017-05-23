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
        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            // Reply received
            if (message.getPerformative() == ACLMessage.PROPOSE) {
                // This is an offer
                EnergyOffer energyOffer = EnergyOffer.deserialize(message.getContent());

                //Store the RemoteEnergyOffer in the priorityQueue
                buyEnergy.addEnergyOffer(new RemoteEnergyOffer(message.getSender(), energyOffer, buyEnergy.getProsumerAgent().getRoutingValueTo(message.getSender().getLocalName())));
            }
        }
        else {
            block();
        }
    }
}
