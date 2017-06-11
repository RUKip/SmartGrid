package com.rug.energygrid.agents.bigGuyAgent;

import com.rug.energygrid.agents.prosumerAgent.ProsumerAgent;
import com.rug.energygrid.agents.prosumerAgent.ProsumerConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellingAgent;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.time.Duration;
import java.time.Instant;

//currently not used
public class BigGuyAgent extends SellingAgent {
    public static final double CONSTANTPRICE = 0.22;     // source http://www.energiesite.nl/begrippen/kwh-prijs/
    public static final double BUYBACKPRICE = 0.15;

    private ServiceDescription sd; //The serviceDescription of the BigGuy

    @Override
    public void setup() {
        super.setup();
        curEnergy = Double.MAX_VALUE;
        sellEnergy.setLocalEnergyPrice(CONSTANTPRICE);
        addToYellowPages();
    }

    /*@Override
    public void timedEvent(Instant end, Duration passedTime) {
        //TODO: here you might add some dynamic price changing
        super.timedEvent(end, passedTime);
    }*/

    private void addToYellowPages() {
        // Register the agent as a Timed instance
        sd = new ServiceDescription();
        sd.setType(BuySellComConstants.BIG_GUY_SD);
        sd.addProperties(new Property(BuySellComConstants.BIG_GUY_ENERGY_PROPERTY, new Double(BUYBACKPRICE)));
        sd.setName("bigGuyAgent");
        addService(sd);
    }

    @Override
    public void subtractCurEnergy(double soldEnergy) {
        //Nothing has to happen since the big guy has infinite energy.
    }
}
