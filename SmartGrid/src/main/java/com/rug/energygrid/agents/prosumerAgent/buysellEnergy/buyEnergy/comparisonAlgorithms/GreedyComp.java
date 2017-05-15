package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

/**
 * Created by Ruben on 02-May-17.
 */
//stupid Algorithm meant for testing, purposes (TODO: more usefull would be take agent location or something and take shortest path)
public class GreedyComp implements Comparator<RemoteEnergyOffer> {

    @Override
    public int compare(RemoteEnergyOffer me, RemoteEnergyOffer others) {
        return (int) (others.getSellingEnergy()-me.getSellingEnergy());
    }
}
