package com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm;

import java.util.HashMap;

public class GraphTuple {

    HashMap<String,Double> finalGraph, lengthGraph;

    public GraphTuple(HashMap<String,Double> finalGraph, HashMap<String,Double> lengthGraph){
            this.finalGraph = finalGraph;
            this.lengthGraph = lengthGraph;
    }

    public HashMap<String,Double> getLengthGraph(){
        return this.lengthGraph;
    }

    public HashMap<String,Double> getFinalGraph(){
        return this.finalGraph;
    }
}
