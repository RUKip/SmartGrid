package com.rug.energygrid.agents.time.timedAgent;

import jade.core.Agent;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 9-5-17.
 */
public abstract class TimedAgent extends Agent {
    @Override
    protected void setup() {
        InitiateTimeBhvr itb = new InitiateTimeBhvr(this);
        this.addBehaviour(itb);
    }

    public abstract void timedEvent(Instant end, Duration duration);
}
