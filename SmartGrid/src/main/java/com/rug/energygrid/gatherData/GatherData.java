package com.rug.energygrid.gatherData;

import jade.core.AID;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 18-5-17.
 */
public class GatherData {
    //Singleton pattern
    public static final String OUTPUT_FILE_NAME = "output.csv";
    public static final GatherData GATHER_DEAL_DATA = new GatherData(new OutputCSV(OUTPUT_FILE_NAME));

    List<TimedEnergyDeal> deals = new ArrayList<>();
    OutputData output;

    public GatherData(OutputData output) {
        this.output = output;
    }

    public synchronized void addDeal(Instant time, AID seller, AID buyer, double price, double energyAmount) {
        deals.add(new TimedEnergyDeal(time, seller, buyer, price, energyAmount));
    }

    public List<TimedEnergyDeal> getDeals() {
        return deals;
    }

    public void createOutput() {
        output.output(this);
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
}
