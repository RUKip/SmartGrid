package com.rug.energygrid.agents.prosumerAgent.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by thijs on 28-4-17.
 */
public class IndividualSellEnergyBehaviour extends Behaviour {
    AID otherAgent;
    double energyToBeSold;
    SellEnergy sellEnergy;
    private MessageTemplate mt; // The template to receive replies
    private int step = 0;
    private boolean finished = false;

    public IndividualSellEnergyBehaviour(Agent parent, AID otherAgent, double energyToBeSold, SellEnergy sellEnergy) {
        super(parent);
        this.otherAgent = otherAgent;
        this.energyToBeSold = energyToBeSold;
        this.sellEnergy = sellEnergy;

        sendOffer();
    }

    private void sendOffer() {
        ACLMessage offer = new ACLMessage(ACLMessage.PROPOSE);
        offer.addReceiver(otherAgent);
        offer.setContent(Double.toString(energyToBeSold));
        offer.setConversationId(BuySellConstants.INDIVIDUALCONVERSTATIONID);
        offer.setReplyWith("offer"+System.currentTimeMillis()); // Unique value
        myAgent.send(offer);
        // Prepare the template to get proposals
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId(offer.getConversationId()),
                MessageTemplate.MatchInReplyTo(offer.getReplyWith()));
    }

    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("got a deal or nodeal Message: "+msg.getContent());
            if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                System.out.println("we have a deal!");

            } else if (msg.getPerformative() == ACLMessage.PROPOSE) {
                System.out.println("No deal, he wants less. Only: "+msg.getContent());
                sellEnergy.addBuyer(msg.getSender(),Double.parseDouble(msg.getContent()));
                sellEnergy.addSurplusEnergy(energyToBeSold);
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
