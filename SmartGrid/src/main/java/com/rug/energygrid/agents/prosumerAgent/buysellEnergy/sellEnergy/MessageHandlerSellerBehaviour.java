package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.logging.LocalLogger;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class MessageHandlerSellerBehaviour extends CyclicBehaviour {
    private static final int MAX_MESSAGE_QUEUE_SIZE = 200;
    private static final Logger logger = LocalLogger.getLogger();
    MessageTemplate mtTransaction = MessageTemplate.and(MessageTemplate.MatchConversationId(BuySellComConstants.TRANSACTION),
                                                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
    SellEnergy sellEnergy;

    public MessageHandlerSellerBehaviour(Agent myAgent, SellEnergy sellEnergy) {
        super(myAgent);
        this.sellEnergy = sellEnergy;
    }

    @Override
    public void action() {
        if (myAgent.getCurQueueSize() > MAX_MESSAGE_QUEUE_SIZE) {
            logger.severe(myAgent.getAID().getLocalName() +": Queue size is larger than MAX");
            int i = 0;
            while (myAgent.getCurQueueSize() > 0) {
                ACLMessage msg = myAgent.receive();
                System.out.println(i+ ", "+msg.getSender().getLocalName()+ " : " + msg.getContent());
                i++;
            }

            System.exit(-1);
        }
        ACLMessage msg = myAgent.receive(mtTransaction);
        if (msg != null) {
            if (msg.getContent() != null) {
                EnergyOffer receivedOffer = EnergyOffer.deserialize(msg.getContent());
                ACLMessage reply = msg.createReply();
                switch (sellEnergy.compareDeal(receivedOffer)) {
                    case SellEnergy.PERFECT_DEAL:
                        //Perfect deal
                    case SellEnergy.LESSTHAN_DEAL:
                        //lessThan deal
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        double sellingEnergy = sellEnergy.reserveEnergy(receivedOffer.getEnergy());
                        reply.setContent(Double.toString(sellingEnergy));
                        sellEnergy.processPayment(receivedOffer.getPrice(), sellingEnergy);
                        break;
                    case SellEnergy.NO_DEAL:
                        //no deal
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
