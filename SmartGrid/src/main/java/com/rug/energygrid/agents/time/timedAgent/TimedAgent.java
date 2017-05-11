package com.rug.energygrid.agents.time.timedAgent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;

/**
 * Created by thijs on 9-5-17.
 */
public abstract class TimedAgent extends Agent {
    DFAgentDescription dfd = new DFAgentDescription();

    @Override
    protected void setup() {
        initializeDFD();
        InitiateTimeBhvr itb = new InitiateTimeBhvr(this);
        this.addBehaviour(itb);
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

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public abstract void timedEvent(Instant end, Duration duration);
}
