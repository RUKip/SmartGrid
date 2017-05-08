package com.rug.energygrid.weather;

import java.time.*;

/**
 * Created by Ruben on 02-May-17.
 */
public class testMain {
    public static void main(String args[]){
        Weather w = new ExampleDataSet_KNI();
        LocalDateTime date = LocalDateTime.of(1971, 6, 3, 0, 0, 1);
        Instant time = date.toInstant(ZoneOffset.UTC);
        System.out.println("Wind speed at time: " + time + " is " + w.getWindSpeed(time));
        System.out.println("Solar irrediance at time: " + time + " is " + w.getSunIrradiation(time));
    }
}
