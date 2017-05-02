package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.EnergyOffer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by thijs on 2-5-17.
 */
public class TransactionHandlerBuyer extends Behaviour{
    BuyEnergy buyEnergy;
    AID otherAgent;
    double energyToBeBought;
    EnergyOffer currentOffer;

    private MessageTemplate mt; // The template to receive replies
    private int step = 0;
    private boolean finished = false;

    public TransactionHandlerBuyer(Agent parent, BuyEnergy buyEnergy, AID otherAgent, double energyToBeBought, EnergyOffer currentOffer) {
        super(parent);
        this.buyEnergy = buyEnergy;
        this.otherAgent = otherAgent;
        this.energyToBeBought = energyToBeBought;
        this.currentOffer = currentOffer;

        sendOffer();
    }

    private void sendOffer() {
        ACLMessage offer = new ACLMessage(ACLMessage.PROPOSE);
        offer.addReceiver(otherAgent);
        offer.setContent(Double.toString(energyToBeBought));//TODO: Add later here also the currentOffer
        offer.setConversationId(BuySellConstants.TRANSACTION);
        offer.setReplyWith("transaction"+System.currentTimeMillis()); // Unique value
        myAgent.send(offer);
        // Prepare the template to get proposals
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId(offer.getConversationId()),
                                 MessageTemplate.MatchInReplyTo(offer.getReplyWith()));
    }

    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("got a deal or no-deal Message: "+msg.getContent());
            if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                double boughtEnergy = Double.parseDouble(msg.getContent());
                if (boughtEnergy < energyToBeBought) {
                    buyEnergy.boughtLessEnergy(energyToBeBought - boughtEnergy);
                }
                System.out.println("bought: "+boughtEnergy+" wanted: "+energyToBeBought+" from: "+myAgent.getLocalName()+" to: "+otherAgent.getLocalName());

            } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                System.out.println("No deal");
            }
            finished = true;
        } else {
            block();
        }
    }

    public boolean done() {
        return finished;
    }
}
}
