package com.rug.energygrid.agents.prosumerAgent.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by thijs on 2-5-17.
 */
public class MessageHandlerSellerBehaviour extends CyclicBehaviour {
    MessageTemplate mtTransaction = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                                                    MessageTemplate.MatchConversationId(BuySellConstants.TRANSACTION));
    SellEnergy sellEnergy;

    public MessageHandlerSellerBehaviour(Agent myAgent, SellEnergy sellEnergy) {
        super(myAgent);
        this.sellEnergy = sellEnergy;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mtTransaction);
        if (msg != null) {
            System.out.println("got a Message: "+msg.getContent());

            double energy = Double.parseDouble(msg.getContent());
            ACLMessage reply = msg.createReply();

            switch (sellEnergy.compareDeal(energy)) {
                case SellEnergy.PERFECT_DEAL:
                    //Perfect deal
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent(Double.toString(sellEnergy.reserveEnergy(energy)));
                    break;
                case SellEnergy.LESSTHAN_DEAL:
                    //lessThan deal
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent(Double.toString(sellEnergy.reserveEnergy(energy)));
                    break;
                case SellEnergy.NO_DEAL:
                    //no deal
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    break;
            }
            myAgent.send(reply);
        } else {
            block();
        }
    }
}
