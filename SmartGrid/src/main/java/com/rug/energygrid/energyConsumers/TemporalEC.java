package com.rug.energygrid.energyConsumers;

import java.time.*;

public abstract class TemporalEC extends ConstantEC {
    private LocalDate lastUsed;

    @Override
    public double consumeEnergy(Instant end, Duration duration) {
        LocalTime localStart = LocalTime.from(end.minus(duration));
        if (localStart.isAfter(startUsing()) && localStart.isBefore(stopUsing())) {
            LocalDate localDate = LocalDate.from(end.minus(duration));
            if (localDate.isEqual(lastUsed)) {
                return energyConsumption();
            }
        }
        return 0;
    }

    public abstract LocalTime startUsing();
    public abstract LocalTime stopUsing();
}
