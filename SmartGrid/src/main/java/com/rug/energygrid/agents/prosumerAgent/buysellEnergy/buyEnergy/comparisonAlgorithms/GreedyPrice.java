package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

/**
 * Created by thijs on 16-5-17.
 */
public class GreedyPrice {

    //does a greedy comparison on the price of an offer (TODO: more usefull would be take agent location or something and take shortest path)
    public class GreedyEnergy implements Comparator<RemoteEnergyOffer> {

        @Override
        public int compare(RemoteEnergyOffer me, RemoteEnergyOffer others) {
            return (int) (me.getPrice()-others.getPrice());
        }
    }
}
