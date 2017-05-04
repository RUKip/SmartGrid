package com.rug.energygrid.agents.prosumerAgent;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.BuyEnergy;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellEnergy;
import jade.core.Agent;

/**
 * Created by thijs on 28-4-17.
 */
public class ProsumerAgent extends Agent {
    private double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    private double realEnergy = 0; //This is the real amount of energy (if for example energy is sold it will be subtracted from this.
    private BuyEnergy buyEnergy;
    private SellEnergy sellEnergy;

    @Override
    protected void setup() {
        final double startEnergy = Double.parseDouble((String) this.getArguments()[0]);
        System.out.println("name: "+getAID().getName()+" energy: "+ startEnergy);
        curEnergy = startEnergy;
        buyEnergy = new BuyEnergy(this);
        sellEnergy = new SellEnergy(this);
    }

    //Used when a behaviour sold or bought energy, the real energy left in the system has to be updated.
    public void updateRealEnergy(double energy) {
        //Is negated since if you sell energy the realenergy goes down.
        realEnergy -= energy;
    }

    public void addCurEnergy(double energy) {
        curEnergy += energy;
    }

    public void subtractCurEnergy(double energy) {
        curEnergy -= energy;
        buyEnergy.refillEnergy();
    }

    public double getCurEnergy() {
        return curEnergy;
    }
}
