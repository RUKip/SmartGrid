package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.AID;

public class RemoteEnergyOffer {
    private AID agent;
    public EnergyOffer energyOffer;
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

    public double getEnergy(){
        return energyOffer.getEnergy();
    }

    public double getPrice() {
        return energyOffer.getPrice();
    }

    public double getCableEnergyLoss(){return cableEnergyLoss;}

    public double getCableTotalResistance(){return this.cableTotalResistance;}

    //Calculates what is the max energy that can be sold to this buyer
    public double calcEnergyToBeBought(double neededEnergy) {
        double energyLossIncluded = neededEnergy/cableEnergyLoss; //Here the amount of energy lost is calculated, see cable.getCost() and http://large.stanford.edu/courses/2010/ph240/harting1/
        return energyOffer.getEnergy() <= energyLossIncluded ? (energyOffer.getEnergy()) : energyLossIncluded;
    }

    public double getEnergyLeft() {
        return getEnergy()*cableEnergyLoss;
    }

    public MaxAndRemaining getMaxAndRemaining(double neededEnergy) {
        RemoteEnergyOffer max = new RemoteEnergyOffer(agent, new EnergyOffer(this.getPrice(), calcEnergyToBeBought(neededEnergy)), this.getCableTotalResistance());
        RemoteEnergyOffer remaining = null;
        if (max.getEnergy() < this.getEnergy()){
            remaining = new RemoteEnergyOffer(agent, new EnergyOffer(this.getPrice(), this.getEnergy() - max.getEnergy()), this.getCableTotalResistance());
        }
        return new MaxAndRemaining(max, remaining);
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

    public class MaxAndRemaining {
        public RemoteEnergyOffer max;
        public RemoteEnergyOffer remaining;

        public MaxAndRemaining(RemoteEnergyOffer max, RemoteEnergyOffer remaining) {
            this.max = max;
            this.remaining = remaining;
        }
    }
}
