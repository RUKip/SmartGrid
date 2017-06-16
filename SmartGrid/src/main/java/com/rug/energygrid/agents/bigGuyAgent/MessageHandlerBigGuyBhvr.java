package com.rug.energygrid.agents.bigGuyAgent;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellEnergy;
import com.rug.energygrid.logging.LocalLogger;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * Created by thijs on 16-6-17.
 */
public class MessageHandlerBigGuyBhvr extends CyclicBehaviour{
    private static final Logger logger = LocalLogger.getLogger();
    MessageTemplate mtTransaction = MessageTemplate.and(MessageTemplate.MatchConversationId(BuySellComConstants.BIG_GUY_SELL_MESSAGE),
                                                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
    BigGuyAgent bigGuy;

    public MessageHandlerBigGuyBhvr(BigGuyAgent bigGuy) {
        super(bigGuy);
        this.bigGuy = bigGuy;
    }

    public void action() {
        ACLMessage msg = myAgent.receive(mtTransaction);
        if (msg != null) {
            if (msg.getContent() != null) {
                EnergyOffer receivedOffer = EnergyOffer.deserialize(msg.getContent());
                ACLMessage reply = msg.createReply();
                if (bigGuy.checkBuyBackOffer(receivedOffer)) {
                    //deal accepted
                    //lessThan deal
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    //we currently don't reduce the energy of Big guy, since it has infinite energy.
                    reply.setContent(Double.toString(receivedOffer.getEnergy()));
                } else {
                    //no deal
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                }
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
