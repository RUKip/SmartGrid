package com.rug.energygrid.energyConsumers;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 8-5-17.
 */
public interface energyConsumer {
    double consumeEnergy(Instant start, Duration duration);
}
