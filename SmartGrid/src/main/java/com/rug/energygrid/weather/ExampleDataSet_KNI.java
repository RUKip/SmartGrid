package com.rug.energygrid.weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Ruben on 26-Apr-17.
 */

//Static example, API would call specific dataSet in each method (This has to be implemented for
public class ExampleDataSet_KNI extends Weather {

    public ExampleDataSet_KNI(){
        super();
        this.dataSet = readDataSet("weatherDataKNI.txt", ",", " ", "-"); //This is done because of Constant set
    }

    //These methods change depending on constant set or API
    public Double getWindSpeed(Instant time) {
        int iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(WIND_SPEED_POS);
    }

    public Integer getWindDirection(Instant time) {
        int iTime = convertToIntOfDataSet(time);
        Double value = dataSet.get(iTime).get(WIND_DIRECTION_POS);
        if(value == null) return null;
        return value.intValue();
    }

    public Double getSunIrradiation(Instant time) {
        int iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(SOLAR_IRRIDIANCE_POS);
    }

    //when using data set has to be implemented
    protected int convertToIntOfDataSet(Instant time){
        LocalDateTime ldt = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        String monthAdditive = "", dayAdditive = "";
        if(ldt.getMonthValue()<10) monthAdditive = "0";
        if(ldt.getDayOfMonth()<10) dayAdditive = "0";
        return Integer.valueOf("" + ldt.getYear() + monthAdditive + ldt.getMonthValue() + dayAdditive + ldt.getDayOfMonth());
    }

    //These are the values that have to be set depending on your dataset indexing
    public int getTimePos() {
        return 1;
    }

    public int getWindDirPos() {
        return 2;
    }

    public int getWindSpeedPos() {
        return 3;
    }

    public int getSolarIrrPos() {
        return 20;
    }
}
