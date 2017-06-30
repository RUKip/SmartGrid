package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

//stupid Algorithm meant for testing purposes, will take the most offering energy offer
public class GreedyEnergy implements Comparator<RemoteEnergyOffer> {

    @Override
    public int compare(RemoteEnergyOffer me, RemoteEnergyOffer others) {
        //TODO: this has to change to something with cable energy loss
        return (int) ((others.getEnergy() - others.getCableTotalResistance())-(me.getEnergy() - me.getCableTotalResistance()));
    }
}
