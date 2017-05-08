package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

/**
 * Created by Ruben on 08-May-17.
 */
//TODO: simple version without details like maxcurrent etc.
public class Cable {

    private String originNode, connectedNode;
    private double length, resistance;

    public Cable(String originNode, String connectedNode, double length, double resistance){
        this.originNode = originNode;
        this.connectedNode = connectedNode;
        this.length = length;
        this.resistance = resistance;
    }

    public String getOriginNode(){
        return this.originNode;
    }

    public String getConnectedNode(){
        return this.connectedNode;
    }

    public double getCost(){ //TODO: probably needs to be extended
        return (length*resistance);
    }
}
