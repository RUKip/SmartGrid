package com.rug.energygrid.agents.prosumerAgent;

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

    public void add(EnergyOffer e1) {
        int position = size;
        if (size == 0) {
            this.queue.add(e1);
            this.size++;
        } else {
            EnergyOffer e2 = queue.get(position - 1);
            while (comparator.compare(e1, e2) > 0) {
                position--;
                e2 = queue.get(position - 1);
            }
            this.queue.add(position, e1);
            this.size++;
        }
    }

    public void remove(EnergyOffer e) {
        this.queue.remove(e);
    }

    public void remove(AID agent){
        for(int i =0; i<size; i++) {
            if(this.queue.get(i).getAgent().equals(agent)){
                this.queue.remove(i);
                break;
            }
        }
    }

    //TODO: check if this removes it the right way because of equals override
    public void update(EnergyOffer e1) {
        if (size == 0) return;
        int pos = 0;
        EnergyOffer e2 = queue.get(pos);
        while (!e1.equals(e2)){
            e2 = queue.get(pos);
            pos++;
        }
        queue.set(pos, e1);
    }

    public void setComparator(Comparator c){
        this.comparator = c;
    }

}
