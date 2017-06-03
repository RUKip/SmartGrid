package com.rug.energygrid.agents.bigGuyAgent;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;

import java.time.Duration;
import java.time.Instant;

//currently not used
public class BigGuyAgent extends ProsumerAgent {
    public static final double CONSTANTPRICE = 0.22;     // source http://www.energiesite.nl/begrippen/kwh-prijs/
    public static final double BUYBACKPRICE = 0.15;
    private boolean sendOffer = false;

    @Override
    public void setup() {
        super.setup();
        curEnergy = Double.MAX_VALUE;
        sellEnergy.setLocalEnergyPrice(CONSTANTPRICE);
        BigGuyHolder.setBigGuy(this.getAID());
    }

    @Override
    public void timedEvent(Instant end, Duration passedTime) {
        //This should be executed one time.
        if (!sendOffer) {
            sellEnergy.sellSurplussEnergy();
            sendOffer = true;
        }
        curEnergy = Double.MAX_VALUE;
        //TODO: here you might add some dynamic price changing
    }
}