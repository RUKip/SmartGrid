package com.rug.energygrid.agents.prosumerAgent;

/**
 * Created by Ruben on 01-May-17.
 */
public abstract class CompAlgorithm {

    public abstract double calcValue(EnergyOffer me, EnergyOffer others);
}
