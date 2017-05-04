package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import jade.core.AID;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ruben on 02-May-17.
 */
public class CustomPriorityQueue {

    private List<EnergyOffer> queue;
    private int size = 0;
    private Comparator comparator;

    public CustomPriorityQueue(Comparator c) {
        this.queue = new ArrayList();
        this.comparator = c;
    }

    public void remove(EnergyOffer e) {
        this.queue.remove(e);
    }

    public void remove(AID agent){
        for(int i =0; i<size; i++) {
            if(this.queue.get(i).getAgent().equals(agent)){
                this.queue.remove(i);
                return;
            }
        }
    }

    //Tries to get curEnergy from the first element
    public EnergyOffer pop(double curEnergy) {
        EnergyOffer realOffer = queue.remove(0);
        double energyToBeBought = Math.min(realOffer.getSellingEnergy(), curEnergy);
        //If the whole offer is used up remove it, otherwise update it.
        if (energyToBeBought == realOffer.getSellingEnergy()) {
            return realOffer;
        } else {
            this.add(new EnergyOffer(realOffer.getAgent(),realOffer.getSellingEnergy()-energyToBeBought));
            return new EnergyOffer(realOffer.getAgent(), energyToBeBought);
        }
    }

    //TODO: check if this removes it the right way because of equals override
    public void add(EnergyOffer e1) {
        if (size == 0) return;
        int pos = 0;
        EnergyOffer e2 = queue.get(pos);
        for (pos = 0; pos < queue.size(); pos++) {
            if (e1.equals(queue.get(pos))) {
                queue.remove(pos);
                break;
            }
        }
        queue.add(e1);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void setComparator(Comparator c){
        this.comparator = c;
    }

}
