package com.rug.energygrid.agents.time.timedAgent;

import com.rug.energygrid.agents.time.TimerComConstants;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.time.Instant;

/**
 * Created by thijs on 9-5-17.
 */
public class InitiateTimeBhvr extends Behaviour{
    MessageTemplate mt = MessageTemplate.MatchConversationId(TimerComConstants.CONVERSATION_ID);
    private boolean timeReceived = false;
    TimedAgent timedAgent;

    DFAgentDescription dfd;
    ServiceDescription sd;


    public InitiateTimeBhvr(TimedAgent timedAgent) {
        super(timedAgent);
        this.timedAgent = timedAgent;
        addToYellowPages();
    }

    private void addToYellowPages() {
        // Register the agent as a consumer
        System.out.println(timedAgent.getAID().getName());
        dfd = new DFAgentDescription();
        dfd.setName(timedAgent.getAID());
        sd = new ServiceDescription();
        sd.setType(TimerComConstants.TIMER_SD);
        sd.setName("timedAgent");
        dfd.addServices(sd);
        try {
            DFService.register(timedAgent, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("ready for time message: "+ Instant.now());
    }

    @Override
    public void action() {
        ACLMessage timeMessage = myAgent.receive(mt);
        if (timeMessage != null) {
            // Reply received
            System.out.println("got an timedMessage: "+timeMessage.getContent());
            TimerComConstants.TimeMessageValues tmv = TimerComConstants.timeMessageDeserialize(timeMessage.getContent());
            InitiateSimulationTimeBehaviour(tmv.startTime,tmv.startSimulationTime,tmv.endSimulationTime,tmv.speedup);
            timeReceived = true;
        }
        else {
            block();
        }
    }

    private void InitiateSimulationTimeBehaviour(Instant startTime, Instant startSimulationTime, Instant endSimulationTime, int speedup) {
        SimulationTimeBhvr stb = new SimulationTimeBhvr(timedAgent, startTime, startSimulationTime, endSimulationTime, speedup);
        timedAgent.addBehaviour(stb);
    }

    @Override
    public boolean done() {
        dfd.removeServices(sd);
        return timeReceived;
    }
}
