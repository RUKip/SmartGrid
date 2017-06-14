package com.rug.energygrid.weather;

import com.rug.energygrid.logging.LocalLogger;
import jade.util.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class Weather {

    protected static int TIME_POS;
    protected static int WIND_SPEED_POS;
    protected static int SOLAR_IRRIDIANCE_POS;

    private static final Logger logger = LocalLogger.getLogger();


    protected Map<Long, Map<Integer, Double>> dataSet;

    public Weather() {
        this.TIME_POS = getTimePos();;
        this.WIND_SPEED_POS = getWindSpeedPos();
        this.SOLAR_IRRIDIANCE_POS = getSolarIrrPos();
    }

    public abstract int getTimePos();
    public abstract int getWindSpeedPos();
    public abstract int getSolarIrrPos();

    public Double getWindSpeed(Instant time) throws TimeOutOfBoundsException {
        checkTime(time);
        return getImpWindSpeed(time);
    }       // in m/s

    public Double getSunIrradiation(Instant time) throws TimeOutOfBoundsException {
        checkTime(time);
        return getImpSunIrradiation(time);
    }       // in m/s

    protected abstract Double getImpWindSpeed(Instant time);
    protected abstract Double getImpSunIrradiation(Instant time) throws TimeOutOfBoundsException;  // j/m

    public class TimeOutOfBoundsException extends Exception{}

    //NOTE: should return shortest form for hours, so in case of 01:00 should return 1:00
    protected abstract long convertToIntOfDataSet(Instant time);

    //NOTE: Read data set can only read from one column, thus your time should be unique in that column
    protected Map<Long, Map<Integer, Double>> readDataSet(String fileName, String seperator, String... cleanStrings){
        BufferedReader br = null;
        int nrElements;
        Map<Long, Map<Integer, Double>> dataSet = new HashMap<Long, Map<Integer, Double>>();
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.warning("Could not read weather data set, file: " + fileName + " was not found");
        }
        try {
            String line = br.readLine();

            while (line != null) {
                Map dataRow = new HashMap<Integer, Double>();
                for(String clean : cleanStrings) {
                    line = line.replace(clean, ""); //cleans string before split
                }
                String[] rowEntry = line.split(seperator);
                Long key = Long.parseLong(rowEntry[TIME_POS]);
                nrElements = rowEntry.length;

                for (int i = 0; i < nrElements; i++) {
                    if(i!=TIME_POS){
                        if(rowEntry[i].equals("")){
                            dataRow.put(i, null); //There is no measurement/data for this entry.
                        }else {
                            dataRow.put(i, Double.parseDouble(rowEntry[i]));
                        }
                    }
                }
                dataSet.put(key, dataRow);
                line = br.readLine();
            }

            br.close();

        }catch(IOException e){
            e.printStackTrace();
            logger.warning("An error occurred when parsing the file: " + fileName);
        }
        logger.info("Done parsing weather data set: " + fileName);
        return dataSet;
    }

    protected abstract void checkTime(Instant time) throws TimeOutOfBoundsException;
}
