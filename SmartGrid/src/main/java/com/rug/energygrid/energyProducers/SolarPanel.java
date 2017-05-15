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

    private double surfaceArea, solarRadiation, efficiency,peakOutput,qualityFactor;//Quality factor is another loss of solar energy between 0.5 an 0.7

    public SolarPanel(Weather weather) {
        super(weather);
    }

    @Override
    public double generateMaxEnergy(Instant end, Duration duration) {
        double p1 = surfaceArea*solarRadiation*efficiency*qualityFactor;
        double watt = Math.max(p1, peakOutput);
        double power = watt*(duration.getSeconds()*60);
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
        if(q<0.5 || q>0.7){
            logger.warning("A solar panel quality was set to: "+q+" min is 0.5 and max is 0.7");
            return;
        }
        this.qualityFactor = q;
    }

}
