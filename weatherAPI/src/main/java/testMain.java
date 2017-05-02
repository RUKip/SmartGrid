/**
 * Created by Ruben on 02-May-17.
 */
public class testMain {
    public static void main(String args[]){
        Weather w = new ExampleDataSet_KNI();
        int time = 19710603; //TODO: should be mentioned or convereted to a special TIME object.
        System.out.println("Wind direction at time: " + time + " is " + w.getWindDirection(time));
        System.out.println("Wind speed at time: " + time + " is " + w.getWindSpeed(time));
        System.out.println("Solar irrediance at time: " + time + " is " + w.getSunIrradiation(time));
    }
}
