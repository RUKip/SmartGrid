package com.rug.energygrid.agents.Time;

import com.rug.energygrid.agents.Time.TimerComConstants;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.time.Instant;

/**
 * Created by thijs on 9-5-17.
 */
public class GlobalTimeAgent extends Agent {
    private Instant startSimulationTime;
    private Instant endSimulationTime;
    int speedup;

    private Instant realStartTime;

    protected void setup() {
        startSimulationTime = Instant.parse((String) getArguments()[0]);
        endSimulationTime = Instant.parse((String) getArguments()[1]);
        speedup = Integer.parseInt((String) getArguments()[2]);
        System.out.println("Simulating from: "+startSimulationTime+" till: "+endSimulationTime+" with speedup: "+speedup);
        try {
            //wait untill all other agents registerd in the yellow pages.
            wait(TimerComConstants.YELLOW_PAGES_REGISTER_WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        this.doDelete();
    }
}
