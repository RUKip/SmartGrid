package com.rug.energygrid.weather;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben on 26-Apr-17.
 */
public abstract class Weather {

    protected static int TIME_POS;
    protected static int WIND_DIRECTION_POS;
    protected static int WIND_SPEED_POS;
    protected static int SOLAR_IRRIDIANCE_POS;

    protected Map<Integer, Map<Integer, Double>> dataSet;

    public Weather() {
        this.TIME_POS = getTimePos();;
        this.WIND_DIRECTION_POS = getWindDirPos();
        this.WIND_SPEED_POS = getWindSpeedPos();
        this.SOLAR_IRRIDIANCE_POS = getSolarIrrPos();
    }

    public abstract int getTimePos();
    public abstract int getWindDirPos();
    public abstract int getWindSpeedPos();
    public abstract int getSolarIrrPos();

    public abstract Double getWindSpeed(Instant time);       //in m/s
    public abstract Integer getWindDirection(Instant time);      //degree angle
    public abstract Double getSunIrradiation(Instant time);  //J/M

    //when using data set has to be implemented
    protected abstract int convertToIntOfDataSet(Instant time);

    //TODO: finish
    protected Map<Integer, Map<Integer, Double>> readDataSet(String fileName, String seperator, String... cleanStrings){
        BufferedReader br = null;
        int nrElements;
        Map<Integer, Map<Integer, Double>> dataSet = new HashMap<Integer, Map<Integer, Double>>();
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //TODO: Log data file not found here
        }
        try {
            String line = br.readLine();

            while (line != null) {
                Map dataRow = new HashMap<Integer, Double>();
                for(String clean : cleanStrings) {
                    line = line.replace(clean, ""); //cleans string before split
                }
                String[] rowEntry = line.split(seperator);
                Integer key = Integer.parseInt(rowEntry[TIME_POS]);
                nrElements = rowEntry.length;

                //TODO: now parses/stores every entry, we could also only read required onces
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
            //TODO: Log IOException
        }
        return dataSet;
    }
}
