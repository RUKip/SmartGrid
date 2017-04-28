package com.rug.energygrid.agents.prosumerAgent.behaviour;

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

/**
 * Created by thijs on 28-4-17.
 */

//This behaviour tries to sell the surplusEnergy it has
public class InitiateSellEnergyBehaviour extends Behaviour {
    SellEnergy sellEnergy;

    List<AID> prosumerAgentList;
    private int repliesCnt = 0; // The counter of replies from seller agents
    private MessageTemplate mt; // The template to receive replies
    private int step = 1;

    public InitiateSellEnergyBehaviour(Agent parent, double surplusEnergy) {
        super(parent);
        System.out.println("going to sell "+ surplusEnergy);
        // create the list of seller agents
        prosumerAgentList = new ArrayList<AID>();
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(BuySellConstants.BUYTYPE);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            for (int i = 0; i < result.length; ++i) {
                prosumerAgentList.add(result[i].getName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        this.sellEnergy = new SellEnergy(parent, surplusEnergy);

        sendMessageToPotentialBuyers();
    }

    private void sendMessageToPotentialBuyers() {
        // Send the cfp to all potential buyers
        ACLMessage cfp = new ACLMessage(ACLMessage.INFORM);
        for (AID agent : prosumerAgentList) {
            cfp.addReceiver(agent);
        }
        cfp.setContent(Double.toString(sellEnergy.getSurplusEnergy()));
        cfp.setConversationId(BuySellConstants.CONVERSATIONID);
        cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
        myAgent.send(cfp);
        // Prepare the template to get proposals
        mt = MessageTemplate.and(MessageTemplate.MatchConversationId(BuySellConstants.CONVERSATIONID),
                MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
        System.out.println("test: "+cfp.getReplyWith());
    }

    //ASUMES THAT ALL THE OTHER BUYER AGENTS RESPOND, SO NONE CAN LEAVE SUDDENLY
    @Override
    public void action() {
        System.out.println("name: "+getBehaviourName()+"lets sell some energy! "+ sellEnergy.getSurplusEnergy());
        // Receive all proposals/refusals from buyer agents
        ACLMessage reply = myAgent.receive(mt);
        if (reply != null) {
            // Reply received
            System.out.println("got response:"+reply.getContent());
            if (reply.getPerformative() == ACLMessage.PROPOSE) {
                // This is an offer
                double neededEnergy = Double.parseDouble(reply.getContent());

                //Store all the buyers in the buyers list
                if (neededEnergy > 0) {
                    sellEnergy.addBuyer(reply.getSender(), neededEnergy);
                }
            }
            repliesCnt++;
        }
        else {
            block();
        }
    }

    //ASUMES THAT ALL THE OTHER BUYER AGENTS RESPOND, SO NONE CAN LEAVE SUDDENLY
    @Override
    public boolean done() {
        if (repliesCnt >= prosumerAgentList.size()) {
            sellEnergy.divideEnergy();
            return true;
        }
        return false;
    }
}
