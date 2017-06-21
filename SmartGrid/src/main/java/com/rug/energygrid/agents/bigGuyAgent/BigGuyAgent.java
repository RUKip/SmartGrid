package com.rug.energygrid.agents.bigGuyAgent;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.RemoteEnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.SellingAgent;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

//currently not used
public class BigGuyAgent extends SellingAgent {

    private double buyBackPrice = BigGuyConstants.BUYBACKPRICE;
    private ServiceDescription sd; //The serviceDescription of the BigGuy

    @Override
    public void setup() {
        super.setup();
        curEnergy = Double.MAX_VALUE;
        sellEnergy.setLocalEnergyPrice(BigGuyConstants.CONSTANTPRICE);
        this.addBehaviour(new MessageHandlerBigGuyBhvr(this));
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
        sd.setName("bigGuyAgent");
        addService(sd);
    }

    @Override
    public void subtractCurEnergy(double soldEnergy) {
        //Nothing has to happen since the big guy has infinite energy.
    }

    //checks if the deal the seller send is valid.
    public boolean checkBuyBackOffer(EnergyOffer offer) {
        return (offer.getPrice()==buyBackPrice) && (this.curEnergy >= offer.getEnergy());
    }
}
