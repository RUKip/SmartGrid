package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

/**
 * Created by Ruben on 02-May-17.
 */
//stupid Algorithm meant for testing purposes, will take the most offering energy offer
public class GreedyEnergy implements Comparator<RemoteEnergyOffer> {

    @Override
    public int compare(RemoteEnergyOffer me, RemoteEnergyOffer others) {
        return (int) ((others.getSellingEnergy() - others.getEnergyLoss())-(me.getSellingEnergy() - me.getEnergyLoss()));
    }
}
