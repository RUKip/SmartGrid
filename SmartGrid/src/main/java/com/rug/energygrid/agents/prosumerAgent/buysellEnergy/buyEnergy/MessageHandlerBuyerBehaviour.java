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

public class MessageHandlerBuyerBehaviour extends CyclicBehaviour {
    private MessageTemplate mt = MessageTemplate.MatchConversationId(BuySellComConstants.ENERGY_OFFER_MESSAGE);
    private BuyEnergy buyEnergy;

    public MessageHandlerBuyerBehaviour(Agent myAgent, BuyEnergy buyEnergy) {
        super(myAgent);
        this.buyEnergy = buyEnergy;
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(mt);
        while (message != null) {
            // Reply received
            if (message.getPerformative() == ACLMessage.PROPOSE) {
                // This is an offer
                EnergyOffer energyOffer = EnergyOffer.deserialize(message.getContent());

                //Store the RemoteEnergyOffer in the priorityQueue
                double averageResistance = buyEnergy.getProsumerAgent().getRoutingValueTo(message.getSender().getLocalName()) / (buyEnergy.getProsumerAgent().getRoutingLengthTo(message.getSender().getLocalName()));
                buyEnergy.addEnergyOffer(new RemoteEnergyOffer(message.getSender(), energyOffer, averageResistance));
            }
            message = myAgent.receive(mt);
        }
        block();
    }
}
