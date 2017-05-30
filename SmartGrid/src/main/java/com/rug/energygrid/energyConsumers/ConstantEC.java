package com.rug.energygrid.energyConsumers;

import java.time.Duration;
import java.time.Instant;

public abstract class ConstantEC implements EnergyConsumer {

    @Override
    public double consumeEnergy(Instant end, Duration duration) {
        return energyConsumption()*(duration.getSeconds()+Math.pow(10,-9)*duration.getNano());
    }

    public abstract double energyConsumption();
}
