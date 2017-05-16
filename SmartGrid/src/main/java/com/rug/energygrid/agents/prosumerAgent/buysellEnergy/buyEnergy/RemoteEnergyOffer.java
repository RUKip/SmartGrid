package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.AID;

/**
 * Created by Ruben on 01-May-17.
 */
public class RemoteEnergyOffer {
    private AID agent;
    EnergyOffer energyOffer;

    public RemoteEnergyOffer(AID agent, EnergyOffer energyOffer) {
        this.agent = agent;
        this.energyOffer = energyOffer;
    }

    public double getSellingEnergy(){
        return energyOffer.getSellingEnergy();
    }

    public double getPrice() {
        return energyOffer.getPrice();
    }

    //Calculates what is the max energy that can be sold to this buyer
    public double calcEnergyToBeBought(double neededEnergy) {
        return energyOffer.getSellingEnergy() <= neededEnergy ? energyOffer.getSellingEnergy() : neededEnergy;
    }

    public RemoteEnergyOffer remaining(double energyToBeBought) {
        if (energyOffer.getSellingEnergy() > energyToBeBought) {
            EnergyOffer decreasedEnergyOffer = new EnergyOffer(energyOffer.getPrice(), energyOffer.getSellingEnergy()-energyToBeBought);
            return new RemoteEnergyOffer(agent, decreasedEnergyOffer);
        }
        return null;
    }

    public AID getAgent() {
        return agent;
    }

    @Override
    public boolean equals(Object o){
        return (o instanceof RemoteEnergyOffer && ((RemoteEnergyOffer)o).getAgent().equals(this.getAgent()));
    }

    public boolean equals(RemoteEnergyOffer o){
        return (o.getAgent().equals(this.getAgent()));
    }
}
