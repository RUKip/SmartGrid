package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
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
public class MessageHandlerSellerBehaviour extends CyclicBehaviour {
    MessageTemplate mtTransaction = MessageTemplate.and(MessageTemplate.MatchConversationId(BuySellComConstants.TRANSACTION),
                                                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
    SellEnergy sellEnergy;

    public MessageHandlerSellerBehaviour(Agent myAgent, SellEnergy sellEnergy) {
        super(myAgent);
        this.sellEnergy = sellEnergy;
    }

    @Override
    public void action() {
        System.out.println(myAgent.getAID().getName()+ " runnninnng " + myAgent.getCurQueueSize());
        ACLMessage msg = myAgent.receive(mtTransaction);
        if (msg != null) {
            System.out.println("got a Message: "+msg.getContent());
            if (msg.getContent() != null) {
                EnergyOffer receivedOffer = EnergyOffer.deserialize(msg.getContent());
                ACLMessage reply = msg.createReply();
                switch (sellEnergy.compareDeal(receivedOffer)) {
                    case SellEnergy.PERFECT_DEAL:
                        //Perfect deal
                        System.out.println("accept");
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setContent(Double.toString(sellEnergy.reserveEnergy(receivedOffer.getSellingEnergy())));
                        break;
                    case SellEnergy.LESSTHAN_DEAL:
                        //lessThan deal
                        System.out.println("partly");
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setContent(Double.toString(sellEnergy.reserveEnergy(receivedOffer.getSellingEnergy())));
                        break;
                    case SellEnergy.NO_DEAL:
                        //no deal
                        System.out.println("refuse");
                        reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        break;
                }
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
