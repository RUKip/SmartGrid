package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.agents.bigGuyAgent.BigGuyComConstants;
import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;
import com.rug.energygrid.gatherData.GatherData;
import com.rug.energygrid.logging.LocalLogger;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class SellToBigGuyBhvr extends Behaviour {
    private final static Logger logger = LocalLogger.getLogger();
    private final static GatherData gatherData = GatherData.GATHER_DATA;

    private ProsumerAgent prosumerAgent;
    private RemoteEnergyOffer currentOffer;
    private MessageTemplate mt; // The template to receive replies
    private boolean finished = false;

    public SellToBigGuyBhvr(ProsumerAgent parent, double energy) {
        super(parent);

        this.prosumerAgent = parent;
        sendOffer(energy);
    }

    private void sendOffer(double energy) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(BuySellComConstants.BIG_GUY_SD);
        template.addServices(sd);
        ACLMessage energyOffer = new ACLMessage(ACLMessage.PROPOSE);
        energyOffer.setReplyWith("bigGuySell" + System.currentTimeMillis()); // Unique value
        energyOffer.setConversationId(BuySellComConstants.BIG_GUY_SELL_MESSAGE);
        try {
            //We assume a situation where there is only one Big Guy, Therefore it will just take the first one which is found.
            DFAgentDescription[] result = DFService.search(prosumerAgent, template);
            if (result.length < 1) {
                logger.severe("There is no BigGuy in the system, can't sell my surpluss energy");
                prosumerAgent.addCurEnergy(currentOffer.getEnergy());
            } else {
                AID bigGuy = result[0].getName();
                energyOffer.addReceiver(bigGuy);
                //Big guy energy price is currently static.
                currentOffer = new RemoteEnergyOffer(bigGuy,new EnergyOffer(BigGuyComConstants.BUYBACKPRICE, energy), prosumerAgent.getRoutingValueTo(bigGuy.getLocalName()));
                energyOffer.setContent(currentOffer.energyOffer.serialize());
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId(energyOffer.getConversationId()),
                        MessageTemplate.MatchInReplyTo(energyOffer.getReplyWith()));
                prosumerAgent.send(energyOffer);
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

    }

    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                double soldEnergy = Double.parseDouble(msg.getContent());
                if (soldEnergy < currentOffer.getEnergy()) {
                    //TODO: check if this goes properly with energyloss.
                    prosumerAgent.addCurEnergy(currentOffer.getEnergyLeft() - soldEnergy * currentOffer.getCableEnergyLoss());
                }
                this.processPayment(msg.getSender(), currentOffer.getPrice(), soldEnergy);
            } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                prosumerAgent.addCurEnergy(currentOffer.getEnergyLeft());
            }
            finished = true;
        } else {
            block();
        }
    }

    public void processPayment(AID buyer, double price, double amountEnergy) {
        prosumerAgent.addMoney(price * amountEnergy);
        gatherData.addDeal(prosumerAgent.getCurrentTime(), prosumerAgent.getAID(), buyer, price, amountEnergy);
    }

    public boolean done() {
        return finished;
    }
}