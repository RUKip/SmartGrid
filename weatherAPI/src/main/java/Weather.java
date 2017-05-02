import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public abstract Double getWindSpeed(int time);       //in m/s
    public abstract Integer getWindDirection(int time);      //degree angle
    public abstract Double getSunIrradiation(int time);  //J/M


    //TODO: finish, and test
    protected Map<Integer, Map<Integer, Double>> readDataSet(String fileName, String seperator){
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

            //int cnt = 0;
            while (line != null) {
                //cnt++;
                Map dataRow = new HashMap<Integer, Double>();
                line = line.replace(" ", ""); //cleans string before split
                String[] rowEntry = line.split(seperator);
                Integer key = Integer.parseInt(rowEntry[TIME_POS]);
                nrElements = rowEntry.length;

                //TODO: now parses/stores every entry, we could also only read required onces, also remove all debug print statements
                //System.out.println("Line: " + cnt);
                for (int i = 0; i < nrElements; i++) {
                    if(i!=TIME_POS){
                        if(rowEntry[i].equals("")){
                            dataRow.put(i, null); //There is no measurement/data for this entry.
                        }else {
                            //System.out.println("read number: " + rowEntry[i]);
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
