package com.rug.energygrid.weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ruben on 26-Apr-17.
 */

//Static example, API would call specific dataSet in each method (This has to be implemented for
public class ExampleDataSet_KNI extends Weather {

    public ExampleDataSet_KNI(){
        super();
        this.dataSet = readDataSet("weatherDataKNI.txt", ",", " ", "-"); //This is done because of Constant set //TODO: this can be speed up, now we read/parse everytime we create a document, but we could also read once and copy the data for each weather
    }

    //These methods change depending on constant set or API
    public Double getWindSpeed(Instant time) {
        int iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(WIND_SPEED_POS);
    }

    public Double getSunIrradiation(Instant time) {
        int iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(SOLAR_IRRIDIANCE_POS);
    }

    //when using data set has to be implemented
    protected int convertToIntOfDataSet(Instant time) {
        LocalDateTime ldt = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return Integer.valueOf(ldt.format(formatter));
    }

    //These are the values that have to be set depending on your dataset indexing
    public int getTimePos() {
        return 1;
    }

    public int getWindSpeedPos() {
        return 3;
    }

    public int getSolarIrrPos() {
        return 20;
    }
}
