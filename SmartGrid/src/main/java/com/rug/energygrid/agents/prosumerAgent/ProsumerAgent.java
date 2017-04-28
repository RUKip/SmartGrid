package com.rug.energygrid.agents.prosumerAgent;

import com.rug.energygrid.agents.prosumerAgent.behaviour.BuyEnergyBehaviour;
import com.rug.energygrid.agents.prosumerAgent.behaviour.BuySellConstants;
import com.rug.energygrid.agents.prosumerAgent.behaviour.InitiateSellEnergyBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Created by thijs on 28-4-17.
 */
public class ProsumerAgent extends Agent {
    private double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    private double realEnergy = 0; //This is the real amount of energy (if for example energy is sold it will be subtracted from this.

    @Override
    protected void setup() {
        final double startEnergy = Double.parseDouble((String) this.getArguments()[0]);
        System.out.println("name: "+getAID().getName()+" energy: "+ startEnergy);
        curEnergy = startEnergy;
        realEnergy = startEnergy;
        if (startEnergy > 0) {
            this.addBehaviour(new InitiateSellEnergyBehaviour(this, curEnergy));
            curEnergy = 0;
        } else if (startEnergy < 0) {
            // Register the book-selling service in the yellow pages
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setType(BuySellConstants.BUYTYPE);
            sd.setName("JADE - book - buying");
            dfd.addServices(sd);
            try {
                DFService.register(this, dfd);
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }

            this.addBehaviour(new BuyEnergyBehaviour(this, curEnergy*-1));
            curEnergy = 0;
        }
    }

    //Used when a behaviour sold or bought energy, the real energy left in the system has to be updated.
    public void updateRealEnergy(double energy) {
        //Is negated since if you sell energy the realenergy goes down.
        realEnergy -= energy;
    }

    public double getCurEnergy() {
        return curEnergy;
    }
}
