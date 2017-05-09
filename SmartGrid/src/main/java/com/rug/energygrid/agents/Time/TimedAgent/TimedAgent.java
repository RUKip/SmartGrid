package com.rug.energygrid.agents.Time.TimedAgent;

import jade.core.Agent;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by thijs on 9-5-17.
 */
public abstract class TimedAgent extends Agent {
    public TimedAgent() {
        InitiateTimeBhvr itb = new InitiateTimeBhvr(this);
        this.addBehaviour(itb);
    }

    public abstract void timedEvent(Instant end, Duration duration);
}
