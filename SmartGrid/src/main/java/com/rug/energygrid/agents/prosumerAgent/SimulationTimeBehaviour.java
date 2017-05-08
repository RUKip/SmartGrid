package com.rug.energygrid.agents.prosumerAgent;

import jade.core.behaviours.Behaviour;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by s2752077 on 5/4/17.
 */
public class SimulationTimeBehaviour extends Behaviour {
    private ProsumerAgent prosumerAgent;
    private Instant startSimulationTime;
    private Instant endSimulationTime;
    private long speedup;

    private Instant prevStep;

    private Instant simulationTime;
    private Duration passedTime;

    public SimulationTimeBehaviour(ProsumerAgent prosumerAgent, Instant startSimulationTime, Instant endSimulationTime, long speedup) {
        this.prosumerAgent = prosumerAgent;
        this.startSimulationTime = startSimulationTime;
        this.simulationTime = startSimulationTime;
        this.endSimulationTime = endSimulationTime;
        this.speedup = speedup;
        this.prevStep = Instant.now();
    }

    @Override
    public void action() {
        Instant curStep = Instant.now();
        passedTime = Duration.between(prevStep, curStep);
        passedTime.multipliedBy(speedup);
        simulationTime.plus(passedTime); //TODO: if this shows pileup problems than change to distcrete calculation using the starttime of the program.
    }
    //To get the middle of the step do simulationTime.minus(passedTime/2);

    @Override
    //The whole simulation is finished. so the agent can stop.
    public boolean done() {
        prosumerAgent.doDelete();
        return !simulationTime.isBefore(endSimulationTime);
    }
}
