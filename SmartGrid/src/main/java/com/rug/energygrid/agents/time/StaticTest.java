package com.rug.energygrid.agents.time;

/**
 * Created by thijs on 12-5-17.
 */
public class StaticTest {
    private static int counter = 0;

    public static void increment() {
        counter++;
        System.out.println(counter);
    }
}
