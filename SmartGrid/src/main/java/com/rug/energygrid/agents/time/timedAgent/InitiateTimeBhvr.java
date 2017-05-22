package com.rug.energygrid.agents.time.timedAgent;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.BuySellComConstants;
import com.rug.energygrid.agents.time.TimerComConstants;
import com.rug.energygrid.agents.time.TimingException;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jdk.nashorn.internal.runtime.Timing;

import java.time.Instant;
import java.util.concurrent.TimeoutException;

/**
 * Created by thijs on 9-5-17.
 */
public class InitiateTimeBhvr extends Behaviour{
    MessageTemplate mt = MessageTemplate.MatchConversationId(TimerComConstants.CONVERSATION_ID);
    private boolean timeReceived = false;
    TimedAgent timedAgent;

    ServiceDescription sd;


    public InitiateTimeBhvr(TimedAgent timedAgent) {
        super(timedAgent);
        this.timedAgent = timedAgent;
        addToYellowPages();
    }

    private void addToYellowPages() {
        // Register the agent as a Timed instance
        sd = new ServiceDescription();
        sd.setType(TimerComConstants.TIMER_SD);
        sd.setName("timedAgent");
        timedAgent.addService(sd);
    }

    @Override
    public void action() {
        ACLMessage timeMessage = myAgent.receive(mt);
        if (timeMessage != null) {
            // Reply received
            TimerComConstants.TimeMessageValues tmv = TimerComConstants.timeMessageDeserialize(timeMessage.getContent());
            try {
                checkStartTime(tmv.startTime);
            } catch (TimingException e) {
                e.printStackTrace();
            }
            InitiateSimulationTimeBehaviour(tmv.startTime,tmv.startSimulationTime,tmv.endSimulationTime,tmv.speedup);
            timeReceived = true;
        }
        else {
            block();
        }
    }

    private void InitiateSimulationTimeBehaviour(Instant startTime, Instant startSimulationTime, Instant endSimulationTime, int speedup) {
        SimulationTimeBhvr stb = new SimulationTimeBhvr(timedAgent, startTime, startSimulationTime, endSimulationTime, speedup);
        timedAgent.addSimulationTimeBhvr(stb);
    }

    //Check if the current time is before the start time.
    private void checkStartTime(Instant startTime) throws TimingException{
        if (!startTime.isAfter(Instant.now())) {
            throw new TimingException();
        }
    }

    @Override
    public boolean done() {
        if (timeReceived) {
            timedAgent.removeService(sd);
            return true;
        } else {
            return false;
        }
    }
}
