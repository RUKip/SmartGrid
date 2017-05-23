package com.rug.energygrid.energyProducers;

import com.rug.energygrid.logging.LocalLogger;
import com.rug.energygrid.weather.Weather;

import java.time.Duration;
import java.time.Instant;

import jade.util.Logger;

/**
 * Created by Ruben on 04-May-17.
 */
public class SolarPanel extends WeatherDependantEP{

    private static Logger logger = LocalLogger.getLogger();

    //Most of the values have to be set in the UI, now the data is preset to what we got from the paper (see Windmill)
    private double surfaceArea = 1.65;
    private double solarRadiation = 40; //Warning: Just a random day in Groningen May 1, 2017 data found on https://www.wunderground.com/personal-weather-station/dashboard?ID=IGRONING36
    private double efficiency = 0.192;
    private double peakOutput = 315;
    private double qualityFactor = 0.75; //Quality factor is another loss of solar energy between 0.5 an 0.9

    public SolarPanel(Weather weather) {
        super(weather);
    }

    @Override
    public double generateMaxEnergy(Instant end, Duration duration) {
        try {
            solarRadiation = weather.getSunIrradiation(end);
        } catch (Weather.TimeOutOfBoundsException e) {
            e.printStackTrace();
        }
        double p1 = surfaceArea*solarRadiation*efficiency*qualityFactor;
        double watt = Math.min(p1, peakOutput);
        double power = watt*(duration.getSeconds()+Math.pow(10,-9)*duration.getNano());
        return power;
    }



    public void setSurfaceArea(double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public void setSolarRadiation(double solarRadiation) {
        this.solarRadiation = solarRadiation;
    }

    public void setPeakOutput(double peakOutput) {
        this.peakOutput = peakOutput;
    }

    private void setEfficiency(double efficiency){
        if(efficiency<0 || efficiency>1){
            logger.warning("A solar panel efficiency was set to: "+efficiency+" min is 0 and max is 1");
            return;
        }
        this.efficiency = efficiency;
    }

    private void setQualityFactor(double q){
        if(q<0.5 || q>0.9){
            logger.warning("A solar panel quality was set to: "+q+" min is 0.5 and max is 0.9");
            return;
        }
        this.qualityFactor = q;
    }

}
