package com.rug.energygrid.agents.prosumerAgent;

import jade.core.AID;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thijs on 18-5-17.
 */
public class GatherDealData {
    //Singleton pattern
    public static final GatherDealData GATHER_DEAL_DATA = new GatherDealData();

    List<TimedEnergyDeal> deals = new ArrayList<>();

    public synchronized void addDeal(Instant time, AID seller, AID buyer, double price, double energyAmount) {
        deals.add(new TimedEnergyDeal(time, seller, buyer, price, energyAmount));
    }

    private class TimedEnergyDeal {
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

    private static final char DEFAULT_SEPARATOR = ',';

    private String addSeperators(String ... fields) {
        String seperated = "";
        boolean first = true;
        for (String field: fields) {
            if (!first) {
                seperated += DEFAULT_SEPARATOR;
            }
            seperated += field;
            first = false;
        }
        return seperated;
    }

    public void createCSVFile(String name) {
        try {
            File csvFile = new File(name);
            if (!csvFile.exists()) {
                csvFile.createNewFile();
            }
            PrintWriter writer = new PrintWriter(csvFile);
            writeDeals(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeDeals(PrintWriter writer) {
        writer.write(addSeperators("Time", "Seller", "Buyer", "price", "amountEnergy")+"\n");
        for (TimedEnergyDeal ted: deals) {
            writer.write(addSeperators(ted.time.toString(),ted.seller!=null?ted.seller.getLocalName():"BigGuy",ted.buyer.getLocalName(),Double.toString(ted.price),Double.toString(ted.energyAmount))+"\n");
        }
    }
}
