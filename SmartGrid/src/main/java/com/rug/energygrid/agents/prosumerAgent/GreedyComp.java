package com.rug.energygrid.agents.prosumerAgent;

import java.util.Comparator;

/**
 * Created by Ruben on 02-May-17.
 */
//stupid Algorithm meant for testing, purposes (TODO: more usefull would be take agent location or something and take shortest path)
public class GreedyComp implements Comparator<EnergyOffer> {

    @Override
    public int compare(EnergyOffer me, EnergyOffer others) {
        return (int) (me.getSellingEnergy() - others.getSellingEnergy());
    }
}
