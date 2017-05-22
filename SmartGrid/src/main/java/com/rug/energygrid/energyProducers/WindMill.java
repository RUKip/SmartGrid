package com.rug.energygrid.energyProducers;

import com.rug.energygrid.logging.LocalLogger;
import com.rug.energygrid.weather.Weather;
import jade.util.Logger;

import java.time.Duration;
import java.time.Instant;

public class WindMill extends WeatherDependantEP{

    private static Logger logger = LocalLogger.getLogger();


    private double airDensity = 1.225;  //airDensity = 1.225; Default because representive of normal airDensity Groningen
    private double areaBlades = (Math.pow((5.5/2),2)*Math.PI); //Default is 5.5 radius because of a smaller wind turbine, The Britwind(Evance)R9000 (same as paper)
    private double maxPower = 5700; //The maximum power output of The R9000 is 5700
    private double efficiency = 0.35; //The power efficiency of The R9000 is about 0.35

    public WindMill(Weather weather) {
        super(weather);
    }

    @Override
    public double generateMaxEnergy(Instant end, Duration duration) {

        double windSpeed = 0;
        try {
            windSpeed = weather.getWindSpeed(end.minus(duration.dividedBy(2)));
        } catch (Weather.TimeOutOfBoundsException e) {
            e.printStackTrace();
        }
        double p1 = 0.5*areaBlades*airDensity*(Math.pow(windSpeed,3))*efficiency;
        double watt = Math.min(p1, maxPower);
        double power = watt*(duration.toMinutes()*60);
        return power;
    }

    private void setDensity(double density){
        this.airDensity = density;
    }

    private void setAreaBlades(double area){
        this.areaBlades = area;
    }

    private void setDiameterBlades(double diameter){
        this.areaBlades = Math.pow((diameter/2),2)*Math.PI;
    }

    private void setMaxPower(double power){
        this.maxPower = power;
    }

    private void setEfficiency(double efficiency){
        if(efficiency<0 || efficiency>1){
            logger.warning("A wind mill efficiency was set to: "+efficiency+" min is 0 and max is 1");
            return;
        }
        this.efficiency = efficiency;
    }
}
