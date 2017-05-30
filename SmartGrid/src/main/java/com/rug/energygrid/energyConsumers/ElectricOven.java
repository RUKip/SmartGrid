package com.rug.energygrid.energyConsumers;

import java.time.LocalTime;

//example of a energy consumer can be used to add to a prosumer
public class ElectricOven extends TemporalEC {

    @Override
    public double energyConsumption() {
        return 7;
    }

    @Override
    public LocalTime startUsing() {
        return LocalTime.of(18,00);
    }

    @Override
    public LocalTime stopUsing() {
        return LocalTime.of(19,00);
    }
}
