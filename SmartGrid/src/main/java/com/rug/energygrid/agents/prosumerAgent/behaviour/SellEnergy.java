package com.rug.energygrid.agents.prosumerAgent.behaviour;

import com.rug.energygrid.agents.prosumerAgent.EnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import jade.core.Agent;

/**
 * Created by thijs on 2-5-17.
 */
public class SellEnergy {
    public static final int PERFECT_DEAL = 0, LESSTHAN_DEAL = 1, NO_DEAL = 2;
    ProsumerAgent myAgent;

    public SellEnergy(ProsumerAgent myAgent) {
        this.myAgent = myAgent;
        myAgent.addBehaviour(new MessageHandlerSellerBehaviour(myAgent, this));
    }

    public int compareDeal(double energyOffer) {
        //There was no offer or no energy is left
        //TODO: IMPLEMENTATION FOR OFFER CHECKING SHOULD BE ADDED AT false LATER
        if (false || myAgent.getCurEnergy() <= 0) {
            return NO_DEAL;
        }

        //There is an offer
        if (myAgent.getCurEnergy() >= energyOffer) {
            return PERFECT_DEAL;
        } else {
            return LESSTHAN_DEAL;
        }
    }

    //reserves min(energyOffer, currentEnergy) at the agent and returns this value.
    public double reserveEnergy(double energyOffer) {
        double soldEnergy = Math.min(myAgent.getCurEnergy(), energyOffer);
        myAgent.subtractCurEnergy(soldEnergy);
        return soldEnergy;
    }
}
