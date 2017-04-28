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

    protected Map<Integer, Map<Integer, Integer>> dataSet;

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

    public abstract double getWindSpeed(int time);       //in m/s
    public abstract int getWindDirection(int time);      //degree angle
    public abstract double getSunIrradiation(int time);  //J/M


    //TODO: finish, and test
    protected Map<Integer, Map<Integer, Integer>> readDataSet(String fileName, String seperator){
        BufferedReader br = null;
        int nrElements;
        Map<Integer, Map<Integer, Integer>> dataSet = new HashMap<Integer, Map<Integer, Integer>>();
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //TODO: Log data file not found here
        }
        try {
            String line = br.readLine();

            while (line != null) {
                Map dataRow = new HashMap<Integer, Integer>();
                line = line.replace(" ", ""); //cleans string before split
                String[] rowEntry = line.split(seperator);
                Integer key = Integer.parseInt(rowEntry[TIME_POS]);
                nrElements = rowEntry.length;

                for (int i = 0; i < nrElements; i++) {
                    if(i!=TIME_POS){
                        dataRow.put(i,Integer.parseInt(rowEntry[i]));
                    }
                }
                dataSet.put(key, dataRow);
            }

            br.close();

        }catch(IOException e){
            e.printStackTrace();
            //TODO: Log IOException
        }
        return dataSet;
    }
}
