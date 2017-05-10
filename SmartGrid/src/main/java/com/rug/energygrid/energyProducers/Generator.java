package com.rug.energygrid.energyProducers;

import java.time.Duration;
import java.time.Instant;

public class Generator implements AdjustableEP {
    @Override
    public double generateMaxEnergy(Instant start, Duration duration) {
        return 0;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public double generateEnergy(Instant start, Duration duration, double energyNeeded) {
        return 0;
    }
}
