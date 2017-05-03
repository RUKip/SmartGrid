package com.rug.energygrid.agents.prosumerAgent;

import jade.core.AID;

import java.util.Comparator;

/**
 * Created by Ruben on 01-May-17.
 */
public class EnergyOffer implements Comparable<EnergyOffer>{
        private AID agent;
        private double sellingEnergy;
        private Comparator<EnergyOffer> algorithm;

        public EnergyOffer(AID agent, double sellingEnergy, Comparator<EnergyOffer> a) {
            this.agent = agent;
            this.sellingEnergy = sellingEnergy;
            this.algorithm = a;
        }

        //Calculates what is the max energy that can be sold to this buyer
        public double calcEnergyToBeBought(double neededEnergy) {
            return sellingEnergy <= neededEnergy ? sellingEnergy : neededEnergy;
        }

        public EnergyOffer remaining(double energyToBeBought) {
            if (sellingEnergy > energyToBeBought) {
                return new EnergyOffer(agent, sellingEnergy-energyToBeBought, algorithm);
            }
            return null;
        }

        public AID getAgent() {
            return agent;
        }

        public double getSellingEnergy(){
            return sellingEnergy;
        }

        public void subtractSellingEnergy(double energy) {
            this.sellingEnergy -= energy;
        }

        @Override
        public boolean equals(Object o){
            return (o instanceof EnergyOffer && ((EnergyOffer)o).getAgent().equals(this.getAgent()));
        }

        @Override
        public int compareTo(EnergyOffer other) {
                //if(buyer == this.getAgent()) return 1; //TODO: build this check in later
                return this.algorithm.compare(this, other);
        }
}
