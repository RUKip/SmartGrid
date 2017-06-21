package com.rug.energygrid.energyProducers;

import com.rug.energygrid.weather.Weather;

import java.time.Duration;
import java.time.Instant;

public interface EnergyProducer {
    public double generateMaxEnergy(Instant end, Duration duration) throws NullPointerException;
    void setWeather(Weather w);
    String getName();
}

