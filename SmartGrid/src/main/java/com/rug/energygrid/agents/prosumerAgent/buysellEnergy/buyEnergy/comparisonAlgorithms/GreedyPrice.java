package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

public class GreedyPrice implements Comparator<RemoteEnergyOffer> {

    @Override
        public int compare(RemoteEnergyOffer me, RemoteEnergyOffer others) {
            return (int) (me.getPrice()-others.getPrice());
        }
}
