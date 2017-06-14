package com.rug.energygrid.weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AmericanDataset extends Weather {

    public AmericanDataset(){
        super();
        this.dataSet = readDataSet("AmericanDataset.csv", ",", " ", "-", ":"); //This is done because of Constant set //TODO: this can be speed up, now we read/parse everytime we create a document, but we could also read once and copy the data for each weather
    }
    @Override
    public int getTimePos() {
        return 0;
    }

    @Override
    public int getWindSpeedPos() {
        return 1;
    }

    @Override
    public int getSolarIrrPos() {
        return 2;
    }

    @Override
    protected Double getImpWindSpeed(Instant time) {
        long iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(WIND_SPEED_POS);
    }

    @Override
    protected Double getImpSunIrradiation(Instant time) throws TimeOutOfBoundsException {
        long iTime = convertToIntOfDataSet(time);
        return dataSet.get(iTime).get(SOLAR_IRRIDIANCE_POS);
    }

    @Override
    protected long convertToIntOfDataSet(Instant time) {
        LocalDateTime ldt = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String value = ldt.format(formatter);
        if(value.charAt(8) == '0'){ //check if hour contains 0 (ex. 01:00) for missing Hour so (position 8)
            value=value.substring(0,8)+value.substring(8+1);
        }
        return Long.valueOf(value);
    }

    @Override
    protected void checkTime(Instant time) throws TimeOutOfBoundsException {
        Instant beginTime = Instant.parse("2005-01-01T00:00:00.00Z");
        Instant endTime = Instant.parse("2005-12-31T23:59:59.00Z");
        if(time.isAfter(endTime) || time.isBefore(beginTime)){
            throw new TimeOutOfBoundsException();
        }
    }
}
