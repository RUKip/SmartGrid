package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by thijs on 2-5-17.
 */
public class TransactionHandlerBuyer extends Behaviour{
    BuyEnergy buyEnergy;
    double energyToBeBought;
    RemoteEnergyOffer currentOffer;
    private MessageTemplate mt; // The template to receive replies
    private boolean finished = false;

    public TransactionHandlerBuyer(Agent parent, BuyEnergy buyEnergy, double energyToBeBought, RemoteEnergyOffer currentOffer) {
        super(parent);
        this.buyEnergy = buyEnergy;
        this.energyToBeBought = energyToBeBought;
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
        System.out.println("sending a energyrequest");
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
                System.out.println("bought: "+boughtEnergy+" wanted: "+energyToBeBought+" from: "+myAgent.getLocalName()+" to: "+currentOffer.getAgent().getLocalName());

            } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                buyEnergy.boughtLessEnergy(energyToBeBought);
                System.out.println("No deal, energy wanted: "+ energyToBeBought);
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

