package com.rug.energygrid.agents.bigGuy;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.ProsumerConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.Agent;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 16-5-17.
 */

//currently not used
public class BigGuyAgent extends ProsumerAgent {
    public static final double CONSTANTPRICE = 0.22;     // source http://www.energiesite.nl/begrippen/kwh-prijs/
    private boolean sendOffer = false;

    @Override
    public void setup() {
        super.setup();
        curEnergy = Double.MAX_VALUE;
        sellEnergy.setLocalEnergyPrice(CONSTANTPRICE);
    }

    @Override
    public void timedEvent(Instant end, Duration passedTime) {
        //This should be executed one time.
        if (!sendOffer) {
            sellEnergy.sellSurplussEnergy();
        }
        curEnergy = Double.MAX_VALUE;
        //TODO: here you might add some dynamic price changing
    }
}
