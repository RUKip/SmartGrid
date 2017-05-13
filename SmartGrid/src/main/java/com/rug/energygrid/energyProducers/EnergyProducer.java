package com.rug.energygrid.energyProducers;

import com.rug.energygrid.weather.Weather;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by s2752077 on 5/4/17.
 */
public interface EnergyProducer {
    double generateMaxEnergy(Instant end, Duration duration);
    void setWeather(Weather w);
}
