package com.rug.energygrid.agents.prosumerAgent.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by thijs on 28-4-17.
 */
public class IndividualBuyEnergyBehaviour extends Behaviour {
    AID otherAgent;
    double energyToBeBought;
    BuyEnergy buyEnergy;
    private MessageTemplate mt; // The template to receive replies
    private int step = 0;
    private boolean finished = false;

    public IndividualBuyEnergyBehaviour(Agent parent, AID otherAgent, double energyToBeBought, BuyEnergy buyEnergy) {
        super(parent);
        this.otherAgent = otherAgent;
        this.energyToBeBought = energyToBeBought;
        this.buyEnergy = buyEnergy;

        sendOffer();
    }

    private void sendOffer() {
        ACLMessage offer = new ACLMessage(ACLMessage.PROPOSE);
        offer.addReceiver(otherAgent);
        offer.setContent(Double.toString(energyToBeBought));
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
                System.out.println("No deal, he has less. Only: "+msg.getContent());
                buyEnergy.addSeller(msg.getSender(),Double.parseDouble(msg.getContent()));
                buyEnergy.addNeededEnergy(energyToBeBought);
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
