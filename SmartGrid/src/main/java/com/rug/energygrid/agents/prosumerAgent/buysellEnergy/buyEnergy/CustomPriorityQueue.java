package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.AID;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomPriorityQueue {

    private List<RemoteEnergyOffer> queue;
    private int size = 0;
    private Comparator comparator;

    public CustomPriorityQueue(Comparator c) {
        this.queue = new ArrayList();
        this.comparator = c;
    }

    public void remove(RemoteEnergyOffer e) {
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
    public RemoteEnergyOffer pop() {
        return queue.remove(0);
    }

    private void checkDuplicate(RemoteEnergyOffer e1) {
        for (int pos = 0; pos < queue.size(); pos++) {
            if (e1.equals(queue.get(pos))) {
                queue.remove(pos);
                return;
            }
        }
    }

    public void add(RemoteEnergyOffer e1) {
        if (e1 == null) {
            return;
        }
        checkDuplicate(e1);
        for (int pos = 0; pos < queue.size(); pos++) {
            if (comparator.compare(e1, queue.get(pos)) < 0) {
                queue.add(pos, e1);
                return;
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

    public int getSize() {
        return queue.size();
    }

    public void print() {
        for (RemoteEnergyOffer offer : queue) {
            System.out.println("energy:" + offer.getEnergy()+"agent: "+offer.getAgent());
        }
    }

}
