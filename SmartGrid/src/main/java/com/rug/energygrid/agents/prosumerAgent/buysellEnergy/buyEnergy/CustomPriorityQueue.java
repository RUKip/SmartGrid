package com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.sellEnergy.EnergyOffer;
import jade.core.AID;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ruben on 02-May-17.
 */
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
    public RemoteEnergyOffer pop(double curEnergy) {
        RemoteEnergyOffer realOffer = queue.remove(0);
        double energyToBeBought = Math.min(realOffer.getSellingEnergy(), curEnergy);
        //If the whole offer is used up remove it, otherwise update it.
        if (energyToBeBought == realOffer.getSellingEnergy()) {
            return realOffer;
        } else {
            this.add(new RemoteEnergyOffer(realOffer.getAgent(), new EnergyOffer(realOffer.getSellingEnergy()-energyToBeBought)));
            return new RemoteEnergyOffer(realOffer.getAgent(), new EnergyOffer(energyToBeBought));
        }
    }

    //TODO: check if this removes it the right way because of equals override
    private void checkDuplicate(RemoteEnergyOffer e1) {
        for (int pos = 0; pos < queue.size(); pos++) {
            if (e1.equals(queue.get(pos))) {
                queue.remove(pos);
                return;
            }
        }
    }

    public void add(RemoteEnergyOffer e1) {
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

}
