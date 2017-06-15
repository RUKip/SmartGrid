package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

/**
 * Created by thijs on 16-5-17.
 */
public class GreedyPrice implements Comparator<RemoteEnergyOffer> {

    @Override
        public int compare(RemoteEnergyOffer me, RemoteEnergyOffer others) {
            //System.out.println("Compare "+me.getAgent().getLocalName()+" : " +others.getAgent().getLocalName() + "");
            if (me.getPrice()-others.getPrice() == 0) {
                return 0;
            } else {
                return me.getPrice()-others.getPrice() < 0 ? -1 : 1;
            }
        }
}
