package com.rug.energygrid.energyConsumers;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 12-5-17.
 */
public class GeneralEnergyConsumer extends ConstantEC {

    //assuming a general dutch 4 persons home source: https://www.energieleveranciers.nl/gemiddeld-energieverbruik
    //Usage per second.
    @Override
    public double energyConsumption() {
        return 519.4063; // watt/s
    }
}
