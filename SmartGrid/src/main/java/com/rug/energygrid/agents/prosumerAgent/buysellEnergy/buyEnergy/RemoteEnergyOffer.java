package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.AID;

public class RemoteEnergyOffer {
    private AID agent;
    EnergyOffer energyOffer;
    private double cableTotalResistance, cableEnergyLoss;

    private static final double INDUCTION_PER_METER = 2.6;
    private static final double SPEED_OF_LIGHT = 3*Math.pow(10,8);

    //TODO: fix this cableEnergyLoss
    public RemoteEnergyOffer(AID agent, EnergyOffer energyOffer, double cableTotalResistance) {
        this.agent = agent;
        this.energyOffer = energyOffer;
        this.cableTotalResistance = cableTotalResistance;
        this.cableEnergyLoss = Math.pow((Math.E),(-1*this.cableTotalResistance)/(INDUCTION_PER_METER*SPEED_OF_LIGHT));
    }

    public double getSellingEnergy(){
        return energyOffer.getSellingEnergy();
    }

    public double getPrice() {
        return energyOffer.getPrice();
    }

    public double getCableEnergyLoss(){return cableEnergyLoss;}

    public double getCableTotalResistance(){return this.cableTotalResistance;}

    //Calculates what is the max energy that can be sold to this buyer
    public double calcEnergyToBeBought(double neededEnergy) {
        double energyLossIncluded = neededEnergy/cableEnergyLoss; //Here the amount of energy lost is calculated, see cable.getCost() and http://large.stanford.edu/courses/2010/ph240/harting1/
        return energyOffer.getSellingEnergy() <= energyLossIncluded ? (energyOffer.getSellingEnergy()) : energyLossIncluded;
    }

    public double calcEnergyLeft(double neededEnergy){
        double energyLossIncluded = neededEnergy/cableEnergyLoss;
        return energyOffer.getSellingEnergy() <= energyLossIncluded ? (energyOffer.getSellingEnergy()*cableEnergyLoss) : neededEnergy;
    }

    public RemoteEnergyOffer remaining(double energyToBeBought) {
        if (energyOffer.getSellingEnergy() > energyToBeBought) {
            EnergyOffer decreasedEnergyOffer = new EnergyOffer(energyOffer.getPrice(), energyOffer.getSellingEnergy()-energyToBeBought);
            return new RemoteEnergyOffer(agent, decreasedEnergyOffer, cableTotalResistance);
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
