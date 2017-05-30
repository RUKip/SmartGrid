package com.rug.energygrid.energyConsumers;

public class GeneralEnergyConsumer extends ConstantEC {

    //assuming a general dutch 4 persons home source: https://www.energieleveranciers.nl/gemiddeld-energieverbruik
    //Usage per second.
    @Override
    public double energyConsumption() {
        return 519.4063; // watt/s
    }
}
