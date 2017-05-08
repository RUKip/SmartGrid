package com.rug.energygrid.energyConsumers;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 8-5-17.
 */
public abstract class ConstantEC implements EnergyConsumer {

    @Override
    public double consumeEnergy(Instant start, Duration duration) {
        return energyConsumption();
    }

    public abstract double energyConsumption();
}
