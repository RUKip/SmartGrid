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

    //This is the cost, the total amount of energy left is this number times the energy
    public double getCost(){
        return (resistance*length);
    }
}
