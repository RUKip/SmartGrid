package com.rug.energygrid.energyProducers;

import java.time.Duration;
import java.time.Instant;

public interface AdjustableEP extends EnergyProducer {
    public double generateEnergy(Instant start, Duration duration, double energyNeeded);
}
