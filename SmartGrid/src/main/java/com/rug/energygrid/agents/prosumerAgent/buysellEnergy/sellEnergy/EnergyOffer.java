package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import java.io.Serializable;

/**
 * Created by thijs on 10-5-17.
 */
public class EnergyOffer {
    protected double sellingEnergy;

    public EnergyOffer(double sellingEnergy) {
        this.sellingEnergy = sellingEnergy;
    }

    public double getSellingEnergy(){
        return sellingEnergy;
    }

    public void subtractSellingEnergy(double energy) {
        this.sellingEnergy -= energy;
    }

    //To send the offer.
    public String serialize() {
        return Double.toString(sellingEnergy);
    }

    //To receive the offer.
    public static EnergyOffer deserialize(String message) {
        return new EnergyOffer(Double.parseDouble(message));
    }
}
