package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.EnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by thijs on 28-4-17.
 */

//This behaviour tries to buy the energy it needs
public class BuyEnergyBehaviour extends Behaviour {
    private double neededEnergy;
    //double startEnergy;
    private PriorityQueue<EnergyOffer> pq;

    private int step;

    public BuyEnergyBehaviour(Agent parent, double neededEnergy) {
        super(parent);

        //Set the variables for the amount of energy that has to be bought.
        //this.startEnergy = neededEnergy;
        this.neededEnergy = neededEnergy;
    }

    @Override
    public void action() {
        System.out.println("name: "+getBehaviourName()+" needs to buy: "+ neededEnergy);
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                                                 MessageTemplate.MatchConversationId(BuySellConstants.CONVERSATIONID));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("got a Message: "+msg.getContent());
            System.out.println("testing: "+msg.getReplyWith());
            ACLMessage reply = msg.createReply();
            if (neededEnergy > 0) {
                // The requested book is available for sale. Reply with the price
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(Double.toString(neededEnergy));
            }
            else {
                // The requested book is NOT available for sale.
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("no interest");
            }
            myAgent.send(reply);
        } else {
            mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                                     MessageTemplate.MatchConversationId(BuySellConstants.INDIVIDUALCONVERSTATIONID));
            msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println("got a real buy Message: "+msg.getContent());
                double offer = Double.parseDouble(msg.getContent());
                ACLMessage reply = msg.createReply();
                if (offer <= neededEnergy) {
                    neededEnergy -= offer;
                    System.out.println("I Approve");
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent("accept");
                } else {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(Double.toString(neededEnergy));
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }

    @Override
    public boolean done() {
        if (neededEnergy <= 0) {
            System.out.println("name: "+getBehaviourName()+" done ");
            return true;
        }
        //((ProsumerAgent) myAgent).updateRealEnergy(startEnergy - neededEnergy);
        return false;
    }
}
