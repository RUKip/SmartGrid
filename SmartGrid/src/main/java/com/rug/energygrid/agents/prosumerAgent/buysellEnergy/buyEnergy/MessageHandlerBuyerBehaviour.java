package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

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
public class MessageHandlerBuyerBehaviour extends CyclicBehaviour {
    MessageTemplate mt = MessageTemplate.MatchConversationId(BuySellComConstants.ENERGY_OFFER_MESSAGE);
    BuyEnergy buyEnergy;
    DFAgentDescription dfd;
    ServiceDescription sd;

    public MessageHandlerBuyerBehaviour(Agent myAgent, BuyEnergy buyEnergy) {
        super(myAgent);
        this.buyEnergy = buyEnergy;
        registerConsumer();
    }

    private void registerConsumer() {
        // Register the agent as a consumer
        dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        sd = new ServiceDescription();
        sd.setType(BuySellComConstants.CONSUMER_SD);
        sd.setName("Buyers");
        dfd.addServices(sd);
        try {
            DFService.register(myAgent, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    @Override
    public void action() {
        ACLMessage reply = myAgent.receive(mt);
        if (reply != null) {
            // Reply received
            System.out.println("got an EnergyOffer: "+reply.getContent());
            if (reply.getPerformative() == ACLMessage.PROPOSE) {
                // This is an offer
                double energy = Double.parseDouble(reply.getContent());

                //Store the EnergyOffer in the priorityQueue
                buyEnergy.addEnergyOffer(new EnergyOffer(reply.getSender(), energy));
            }
        }
        else {
            block();
        }
    }

    //This method is called when the agent shutsdown.
    public void takeDown() {
        dfd.removeServices(sd);
    }
}
