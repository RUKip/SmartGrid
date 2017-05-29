package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.comparisonAlgorithms;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;

import java.util.Comparator;

public class MinLostEnergy implements Comparator<RemoteEnergyOffer> {

    @Override
    public int compare(RemoteEnergyOffer o1, RemoteEnergyOffer o2) {
        if(o1.getCableTotalResistance() < o2.getCableTotalResistance()){
            return -1;
        }else if(o1.getCableTotalResistance() > o2.getCableTotalResistance()){
            return 1;
        }else{
            return 0;
        }
    }
}
