package com.rug.energygrid.weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

//Static example, API would call specific dataSet in each method (This has to be implemented for
public class ExampleDataSet_KNI extends Weather {

    public ExampleDataSet_KNI(){
        super();
        this.dataSet = readDataSet("weatherDataKNI.txt", ",", " ", "-"); //This is done because of Constant set //TODO: this can be speed up, now we read/parse everytime we create a document, but we could also read once and copy the data for each weather
    }

    //These methods change depending on constants of the dataset(or depending on a API)
    protected Double getImpWindSpeed(Instant time) {
        long iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(WIND_SPEED_POS)*(0.1);
    }

    protected Double getImpSunIrradiation(Instant time) {
        long iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(SOLAR_IRRIDIANCE_POS);
    }

    protected long convertToIntOfDataSet(Instant time) {
        LocalDateTime ldt = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return Long.valueOf(ldt.format(formatter));
    }

    @Override
    protected void checkTime(Instant time) throws TimeOutOfBoundsException {
        Instant beginTime = Instant.parse("1951-01-01T10:00:00.00Z");
        Instant endTime = Instant.parse("2017-04-25T10:15:30.00Z");
        if(time.isAfter(endTime) || time.isBefore(beginTime)){
            throw new TimeOutOfBoundsException();
        }
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
