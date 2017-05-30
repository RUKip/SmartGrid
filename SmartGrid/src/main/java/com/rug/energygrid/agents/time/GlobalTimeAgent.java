package com.rug.energygrid.agents.time;

import com.rug.energygrid.logging.LocalLogger;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.time.Duration;
import java.time.Instant;

public class GlobalTimeAgent extends Agent {
    private static final Logger logger = LocalLogger.getLogger();

    private Instant startSimulationTime;
    private Instant endSimulationTime;
    int speedup;

    private Instant realStartTime;

    protected void setup() {
        startSimulationTime = Instant.parse((String) getArguments()[0]);
        endSimulationTime = Instant.parse((String) getArguments()[1]);
        speedup = Integer.parseInt((String) getArguments()[2]);

        //wait untill all other agents registerd in the yellow pages.
        this.doWait(TimerComConstants.YELLOW_PAGES_REGISTER_WAIT_TIME);
        sendGlobalTime();
    }

    //Send the starting time to all the time dependant agents.
    private void sendGlobalTime() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(TimerComConstants.TIMER_SD);
        template.addServices(sd);

        ACLMessage timeMessage = new ACLMessage(ACLMessage.INFORM);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            for (int i = 0; i < result.length; ++i) {
                timeMessage.addReceiver(result[i].getName());
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        realStartTime = Instant.now().plusMillis(TimerComConstants.START_SIMULATION_WAIT_TIME);
        timeMessage.setContent(TimerComConstants.timeMessageSerialize(realStartTime, startSimulationTime, endSimulationTime, speedup));
        timeMessage.setConversationId(TimerComConstants.CONVERSATION_ID);
        this.send(timeMessage);
        printSimulationMessage(realStartTime, startSimulationTime, endSimulationTime, speedup);
        this.doDelete();
    }

    private void printSimulationMessage(Instant realStartTime, Instant startSimulationTime, Instant endSimulationTime, int speedup) {
        Duration realSimulationDuration = Duration.between(startSimulationTime, endSimulationTime).dividedBy(speedup);
        logger.info("Simulating from: "+startSimulationTime+" till: "+endSimulationTime+" with speedup: "+speedup);
        logger.info("Simulation takes: " +realSimulationDuration.getSeconds()+ "s, from: "+ realStartTime+" till: " + realStartTime.plus(realSimulationDuration));
    }
}
