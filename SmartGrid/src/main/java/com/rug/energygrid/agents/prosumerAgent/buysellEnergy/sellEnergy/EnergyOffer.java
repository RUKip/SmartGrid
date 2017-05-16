package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import java.io.Serializable;

/**
 * Created by thijs on 10-5-17.
 */
public class EnergyOffer {
    protected double price; // in euros
    protected double sellingEnergy;

    public EnergyOffer(double price, double sellingEnergy) {
        this.price = price;
        this.sellingEnergy = sellingEnergy;
    }

    public double getPrice() {
        return price;
    }

    public double getSellingEnergy(){
        return sellingEnergy;
    }

    public void subtractSellingEnergy(double energy) {
        this.sellingEnergy -= energy;
    }

    //To send the offer.
    public String serialize() {
        return Double.toString(price)+"_"+Double.toString(sellingEnergy);
    }

    //To receive the offer.
    public static EnergyOffer deserialize(String message) {
        String[] split = message.split("_");
        return new EnergyOffer(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }
}
