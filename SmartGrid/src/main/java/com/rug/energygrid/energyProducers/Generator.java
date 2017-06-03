package com.rug.energygrid.energyProducers;

import com.rug.energygrid.weather.Weather;

import java.time.Duration;
import java.time.Instant;

public class Generator implements AdjustableEP {
    @Override
    public double generateMaxEnergy(Instant start, Duration duration) {
        return 0;
    }

    @Override
    public void setWeather(Weather w) { //TODO: fix
        return;
    }

    @Override
    public double generateEnergy(Instant end, Duration duration, double energyNeeded) {
        return 0;
    }

    public String getName(){
        return "Generator";
    }
}
