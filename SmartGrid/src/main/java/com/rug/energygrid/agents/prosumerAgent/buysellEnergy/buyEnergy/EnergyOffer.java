package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import jade.core.AID;

/**
 * Created by Ruben on 01-May-17.
 */
public class EnergyOffer{
    private AID agent;
    private double sellingEnergy;

    public EnergyOffer(AID agent, double sellingEnergy) {
        this.agent = agent;
        this.sellingEnergy = sellingEnergy;
    }

    //Calculates what is the max energy that can be sold to this buyer
    public double calcEnergyToBeBought(double neededEnergy) {
        return sellingEnergy <= neededEnergy ? sellingEnergy : neededEnergy;
    }

    public EnergyOffer remaining(double energyToBeBought) {
        if (sellingEnergy > energyToBeBought) {
            return new EnergyOffer(agent, sellingEnergy-energyToBeBought);
        }
        return null;
    }

    public AID getAgent() {
        return agent;
    }

    public double getSellingEnergy(){
        return sellingEnergy;
    }

    public void subtractSellingEnergy(double energy) {
        this.sellingEnergy -= energy;
    }

    @Override
    public boolean equals(Object o){
        return (o instanceof EnergyOffer && ((EnergyOffer)o).getAgent().equals(this.getAgent()));
    }
}
