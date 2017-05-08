package com.rug.energygrid.energyProducers;

import com.rug.energygrid.weather.Weather;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by Ruben on 04-May-17.
 */
public class SolarPanel extends WeatherDependantEP{

    public SolarPanel(Weather weather) {
        super(weather);
    }

    @Override
    public double generateMaxEnergy(Instant start, Duration duration) {
        return 10;
    }
}
