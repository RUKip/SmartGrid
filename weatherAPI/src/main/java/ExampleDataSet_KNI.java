import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Ruben on 26-Apr-17.
 */

//Static example, API would call specific dataSet in each method
public class ExampleDataSet_KNI extends Weather {

    public ExampleDataSet_KNI(){
        super();
        this.dataSet = readDataSet("weatherDataKNI.txt", ",", " ", "-"); //This is done because of Constant set
    }

    //These methods change depending on constant set or API
    public Double getWindSpeed(Instant time) {
        return dataSet.get(time).get(WIND_SPEED_POS);
    }

    public Integer getWindDirection(Instant time) {
        Double value = dataSet.get(time).get(WIND_DIRECTION_POS);
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
        return Integer.valueOf("" + ldt.getYear() + ldt.getMonth() + ldt.getDayOfMonth());
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
