package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy;

import com.rug.energygrid.FinishedChecker;
import com.rug.energygrid.agents.time.timedAgent.TimedAgent;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 8-6-17.
 */
public abstract class SellingAgent extends TimedAgent {
    protected double curEnergy = 0; //This is the amount of energy that is currently not anywhere on the market.
    protected double moneyBalance = 0; //The amount of money the prosumer currently has (can be negative) TODO: implement
    protected SellEnergy sellEnergy;

    @Override
    protected void setup() {
        super.setup();
        sellEnergy = new SellEnergy(this);
    }



    @Override
    public void timedEvent(Instant end, Duration duration) {
        sellEnergy.sellSurplussEnergy();
    }

    public abstract void subtractCurEnergy(double soldEnergy);

    public synchronized double getCurEnergy() {
        return curEnergy;
    }

    public void addMoney(double amount) {
        moneyBalance += amount;
    }
    public void subtractMoney(double amount) {
        moneyBalance -= amount;
    }

    //This method is ran when the agent shuts down
    @Override
    public void takeDown() {
        sellEnergy.takeDown();
        super.takeDown();
    }
}
