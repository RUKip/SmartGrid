package com.rug.energygrid.gatherData;

import jade.core.AID;
import jade.core.Agent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GatherData {
    //Singleton pattern
    public static final GatherData GATHER_DATA = new GatherData(new OutputR());

    List<TimedEnergyDeal> deals = new ArrayList<>();
    HashMap<AID, List<TimedProduction>> productions = new HashMap<>();
    HashMap<AID, List<TimedProduction>> energyStatus = new HashMap<>();
    OutputData output;

    public GatherData(OutputData output) {
        this.output = output;
    }

    public synchronized void addDeal(Instant time, AID seller, AID buyer, double price, double energyAmount) {
        deals.add(new TimedEnergyDeal(time, seller, buyer, price, energyAmount));
    }

    public synchronized void addProduction(AID producer, Instant time, double amount) {
        productions.putIfAbsent(producer, new ArrayList<TimedProduction>());
        productions.get(producer).add(new TimedProduction(producer, time, amount));
    }

    public synchronized void addEnergyStatus(AID producer, Instant time, double amount) {
        energyStatus.putIfAbsent(producer, new ArrayList<TimedProduction>());
        energyStatus.get(producer).add(new TimedProduction(producer, time, amount));
    }

    public List<TimedEnergyDeal> getDeals() {
        return deals;
    }

    public HashMap<AID, List<TimedProduction>> getProductions() { return productions; }
    public HashMap<AID, List<TimedProduction>> getEnergyStatus() { return productions; }

    public void createOutput() {
        output.output(this);
    }

    //TODO: currently returns all the agents that have produced something, this might not be enough
    public List<AID> getAgents() {
        List<AID> agents = new ArrayList<>();
        for (List<TimedProduction> productions : productions.values()) {
            agents.add(productions.get(0).producer);
        }
        return agents;
    }

    public class TimedEnergyDeal {
        public Instant time;
        public AID seller;
        public AID buyer;
        public double price;
        public double energyAmount;

        public TimedEnergyDeal(Instant time, AID seller, AID buyer, double price, double energyAmount) {
            this.time = time;
            this.seller = seller;
            this.buyer = buyer;
            this.price = price;
            this.energyAmount = energyAmount;
        }
    }

    public class TimedProduction {
        public AID producer;
        public Instant time;
        public double amount;

        public TimedProduction(AID producer, Instant time, double amount) {
            this.producer = producer;
            this.time = time;
            this.amount = amount;
        }
    }
}
