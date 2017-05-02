package com.rug.energygrid.agents.prosumerAgent;

/**
 * Created by Ruben on 02-May-17.
 */
//stupid Algorithm meant for testing, purposes (TODO: more usefull would be take agent location or something and take shortest path)
public class GreedyComp extends CompAlgorithm {

    @Override
    public double calcValue(EnergyOffer me, EnergyOffer others) {
        return (me.getSellingEnergy() - others.getSellingEnergy());
    }
}
