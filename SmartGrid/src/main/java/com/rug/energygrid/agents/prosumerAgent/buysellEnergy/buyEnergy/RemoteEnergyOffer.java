package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.AID;

public class RemoteEnergyOffer {
    private AID agent;
    EnergyOffer energyOffer;
    private double cableEnergyLoss;

    //TODO: fix this cableEnergyLoss
    public RemoteEnergyOffer(AID agent, EnergyOffer energyOffer, double cableEnergyLoss) {
        this.agent = agent;
        this.energyOffer = energyOffer;
        this.cableEnergyLoss = cableEnergyLoss;
    }

    public double getSellingEnergy(){
        return energyOffer.getSellingEnergy();
    }

    public double getPrice() {
        return energyOffer.getPrice();
    }

    public double getCableEnergyLoss(){return this.cableEnergyLoss;}

    //Calculates what is the max energy that can be sold to this buyer
    public double calcEnergyToBeBought(double neededEnergy) {
        double energyWithoutLoss = neededEnergy/cableEnergyLoss; //Here the amount of energy lost is calculated, see cable.getCost() and http://large.stanford.edu/courses/2010/ph240/harting1/
        return energyOffer.getSellingEnergy() <= energyWithoutLoss ? energyOffer.getSellingEnergy() : energyWithoutLoss;
    }

    public RemoteEnergyOffer remaining(double energyToBeBought) {
        if (energyOffer.getSellingEnergy() > energyToBeBought) {
            EnergyOffer decreasedEnergyOffer = new EnergyOffer(energyOffer.getPrice(), energyOffer.getSellingEnergy()-energyToBeBought);
            return new RemoteEnergyOffer(agent, decreasedEnergyOffer, cableEnergyLoss);
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
