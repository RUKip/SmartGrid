package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TransactionHandlerBuyer extends Behaviour{
    BuyEnergy buyEnergy;
    RemoteEnergyOffer currentOffer;
    private MessageTemplate mt; // The template to receive replies
    private boolean finished = false;

    public TransactionHandlerBuyer(Agent parent, BuyEnergy buyEnergy, RemoteEnergyOffer currentOffer) {
        super(parent);
        this.buyEnergy = buyEnergy;
        this.currentOffer = currentOffer;

        sendOffer();
    }

    private void sendOffer() {
        ACLMessage offer = new ACLMessage(ACLMessage.PROPOSE);
        offer.addReceiver(currentOffer.getAgent());
        offer.setContent(currentOffer.energyOffer.serialize());//TODO: Add later here also the currentOffer
        offer.setConversationId(BuySellComConstants.TRANSACTION);
        offer.setReplyWith("transaction"+System.currentTimeMillis()); // Unique value
        myAgent.send(offer);
        // Prepare the template to get proposals
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId(offer.getConversationId()),
                                 MessageTemplate.MatchInReplyTo(offer.getReplyWith()));
    }

    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                double boughtEnergy = Double.parseDouble(msg.getContent());
                if (boughtEnergy < currentOffer.getEnergy()) {
                    buyEnergy.boughtLessEnergy(currentOffer.getEnergyLeft() - boughtEnergy*currentOffer.getCableEnergyLoss());
                }
                buyEnergy.processPayment(msg.getSender(), currentOffer.getPrice(), boughtEnergy);
            } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                buyEnergy.boughtLessEnergy(currentOffer.getEnergyLeft());
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

