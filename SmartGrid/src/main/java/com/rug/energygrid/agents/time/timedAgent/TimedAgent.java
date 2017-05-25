package com.rug.energygrid.agents.time.timedAgent;

import com.rug.energygrid.agents.time.TimingException;
import com.rug.energygrid.logging.LocalLogger;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;

/**
 * Created by thijs on 9-5-17.
 */
public abstract class TimedAgent extends Agent {
    private Logger logger = LocalLogger.getLogger();
    private DFAgentDescription dfd = new DFAgentDescription();
    private SimulationTimeBhvr simulationTimeBhvr;
    protected Instant curTime; //contains the latest time update.

    @Override
    protected void setup() {
        initializeDFD();
        InitiateTimeBhvr itb = new InitiateTimeBhvr(this);
        this.addBehaviour(itb);
    }

    public Instant getCurrentTime() {
        try {
            if (simulationTimeBhvr == null) {
                throw new TimingException();
            }
        } catch (TimingException e) {
            logger.severe("Tried to retrieve the current time, befor SimulationTimeBhvr is initialized");
            e.printStackTrace();
        }
        return simulationTimeBhvr.getSimulationTime();
    }

    private void initializeDFD() {
        dfd.setName(this.getAID());
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public void addService(ServiceDescription sd) {
        dfd.addServices(sd);
        try {
            DFService.modify(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public void removeService(ServiceDescription sd) {
        dfd.removeServices(sd);
        try {
            DFService.modify(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public void addSimulationTimeBhvr(SimulationTimeBhvr simulationTimeBhvr) {
        this.simulationTimeBhvr = simulationTimeBhvr;
        this.addBehaviour(simulationTimeBhvr);
    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    //This method is performs everything that has to happen when SimulationTimeBhvr does an time instance.
    public void localTimedEvent(Instant end, Duration duration) {
        curTime = end;
        timedEvent(end, duration);
    }

    //This method should be implemented by children that want timed events
    public abstract void timedEvent(Instant end, Duration duration);
}
