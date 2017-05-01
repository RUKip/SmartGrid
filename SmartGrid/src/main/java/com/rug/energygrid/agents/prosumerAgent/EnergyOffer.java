package com.rug.energygrid.agents.prosumerAgent;

import jade.core.AID;
import jade.core.Agent;

/**
 * Created by Ruben on 01-May-17.
 */
public class EnergyOffer implements Comparable<EnergyOffer>{
        private AID agent;
        private double sellingEnergy;
        private CompAlgorithm algorithm;

        public EnergyOffer(AID agent, double sellingEnergy, CompAlgorithm a) {
            this.agent = agent;
            this.sellingEnergy = sellingEnergy;
            this.algorithm = a;
        }

        //Calculates what is the max energy that can be sold to this buyer
        public double calcEnergyToBeSold(double neededEnergy) {
            return sellingEnergy <= neededEnergy ? sellingEnergy : neededEnergy;
        }

        public EnergyOffer remaining(double energyToBeBought) {
            if (sellingEnergy > energyToBeBought) {
                return new EnergyOffer(agent, sellingEnergy-energyToBeBought);
            }
            return null;
        }

        public int getCompareValue(EnergyOffer other){
            this.algorithm.calcValue(this, other);
        }

        public AID getAgent() {
            return agent;
        }

    @Override
    public int compareTo(EnergyOffer other) {
            //if(buyer == this.getAgent()) return 1;
            return (this.getCompareValue(other));
    }
}
