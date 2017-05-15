package com.rug.energygrid.energyProducers;

import com.rug.energygrid.weather.Weather;

public abstract class WeatherDependantEP implements EnergyProducer{

    protected transient Weather weather;

    public WeatherDependantEP(Weather weather){
        this.weather = weather;
    }

    public void setWeather(Weather w){
        this.weather = w;
    }

}
