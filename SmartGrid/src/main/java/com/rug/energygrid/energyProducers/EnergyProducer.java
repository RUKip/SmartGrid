package com.rug.energygrid.energyProducers;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by s2752077 on 5/4/17.
 */
public interface EnergyProducer {
    double generateMaxEnergy(Instant start, Duration duration);
}
