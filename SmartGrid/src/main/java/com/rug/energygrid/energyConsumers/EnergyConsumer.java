package com.rug.energygrid.energyConsumers;

import java.time.Duration;
import java.time.Instant;

public interface EnergyConsumer {
    double consumeEnergy(Instant end, Duration duration);
}
