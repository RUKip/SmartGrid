package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;

/**
 * Created by thijs on 2-5-17.
 */
public class SellEnergy {
    public static final int PERFECT_DEAL = 0, LESSTHAN_DEAL = 1, NO_DEAL = 2;
    ProsumerAgent prosumerAgent;

    public SellEnergy(ProsumerAgent prosumerAgent) {
        this.prosumerAgent = prosumerAgent;
        prosumerAgent.addBehaviour(new MessageHandlerSellerBehaviour(prosumerAgent, this));
    }

    public int compareDeal(double energyOffer) {
        //There was no offer or no energy is left
        //TODO: IMPLEMENTATION FOR OFFER CHECKING SHOULD BE ADDED AT false LATER
        if (false || prosumerAgent.getCurEnergy() <= 0) {
            return NO_DEAL;
        }

        //There is an offer
        if (prosumerAgent.getCurEnergy() >= energyOffer) {
            return PERFECT_DEAL;
        } else {
            return LESSTHAN_DEAL;
        }
    }

    //reserves min(energyOffer, currentEnergy) at the agent and returns this value.
    public double reserveEnergy(double energyOffer) {
        double soldEnergy = Math.min(prosumerAgent.getCurEnergy(), energyOffer);
        prosumerAgent.subtractCurEnergy(soldEnergy);
        return soldEnergy;
    }

    //This method is called when the agent shutsdown.
    public void takeDown() {
        //nothing needs to happen.
    }
}
