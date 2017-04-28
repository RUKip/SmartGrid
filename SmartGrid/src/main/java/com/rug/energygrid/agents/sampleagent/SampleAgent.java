package com.rug.energygrid.agents.sampleagent;
import jade.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by thijs on 25-4-17.
 */
public class SampleAgent extends Agent {
    private static final Logger logger = LoggerFactory.getLogger(SampleAgent.class);

    @Override
    public void setup() {
        final String otherAgentName = (String) this.getArguments()[0];
        System.out.println("ik besta, dus ik ben");
        doDelete();
    }

    @Override
    public void takeDown() {
    }
}