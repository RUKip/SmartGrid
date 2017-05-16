package com.rug.energygrid.agents.bigGuy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.Agent;

/**
 * Created by thijs on 16-5-17.
 */
public class BigGuyAgent extends Agent {
    private final EnergyOffer generalOffer = new EnergyOffer(0.22, 8.0); // source http://www.energiesite.nl/begrippen/kwh-prijs/
}
