package com.rug.energygrid.agents.time.timedAgent;

import jade.core.behaviours.Behaviour;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by s2752077 on 5/4/17.
 */
public class SimulationTimeBhvr extends Behaviour {
    private TimedAgent timedAgent;
    private Instant startSimulationTime;
    private Instant endSimulationTime;
    private long speedup;

    private Instant prevStep;

    private Instant simulationTime;


    public SimulationTimeBhvr(TimedAgent timedAgent, Instant startTime, Instant startSimulationTime, Instant endSimulationTime, long speedup) {
        this.timedAgent = timedAgent;
        this.prevStep = startTime;
        this.startSimulationTime = startSimulationTime;
        this.simulationTime = startSimulationTime;
        this.endSimulationTime = endSimulationTime;
        this.speedup = speedup;
        this.prevStep = Instant.now();
    }

    @Override
    public void action() {
        Instant curStep = Instant.now();
        if (prevStep.isBefore(curStep)) {
            Duration passedTime = Duration.between(prevStep, curStep).multipliedBy(speedup);
            simulationTime = simulationTime.plus(passedTime); //TODO: if this shows pileup problems than change to distcrete calculation using the starttime of the program.
            //System.out.println("simulating: "+startSimulationTime +", "+ simulationTime +", "+ endSimulationTime+ " - "+ passedTime);
            timedAgent.timedEvent(simulationTime, passedTime);
            prevStep = curStep;
        }
    }
    //To get the middle of the step do simulationTime.minus(passedTime/2);

    @Override
    //The whole simulation is finished. so the agent can stop.
    public boolean done() {
        if (!simulationTime.isBefore(endSimulationTime)) {
            System.out.println("Simulation done, shutting down");
            timedAgent.doDelete();
            return true;
        } else {
            return false;
        }
    }
}
