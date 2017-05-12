package com.rug.energygrid.energyConsumers;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 8-5-17.
 */
public abstract class ConstantEC implements EnergyConsumer {

    @Override
    public double consumeEnergy(Instant end, Duration duration) {
        return energyConsumption()*duration.getSeconds();
    }

    public abstract double energyConsumption();
}
