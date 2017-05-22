package com.rug.energygrid.gatherData;

import jade.core.AID;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by thijs on 22-5-17.
 */
public class OutputCSV extends OutputData{
    private String fileName;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone( ZoneId.systemDefault() );

    HashMap<AID, List<GatherData.TimedEnergyDeal>> sellers = new HashMap<>();

    public OutputCSV(String fileName) {
        this.fileName = fileName;
    }

    private void orderDeals(GatherData gatherData) {
        for (GatherData.TimedEnergyDeal ted: gatherData.getDeals()) {
            if (sellers.containsKey(ted.seller)) {
                sellers.get(ted.seller).add(ted);
            } else {
                List<GatherData.TimedEnergyDeal> list = new ArrayList<GatherData.TimedEnergyDeal>();
                list.add(ted);
                sellers.put(ted.seller, list);
            }
        }
    }

    @Override
    public void output(GatherData gatherData) {
        File file = createFile(fileName);
        orderDeals(gatherData);
        writeDeals(file);
        System.out.println("Done with writing deals");
    }

    public File createFile(String name) {
        File csvFile = new File(name);
        try {
            if (!csvFile.exists()) {
                csvFile.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFile;
    }

    private void writeDeals(File csvFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(csvFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (List<GatherData.TimedEnergyDeal> agentDeals: sellers.values()) {
            writer.write("Seller: "+agentDeals.get(0).seller+"\n");
            writer.write(addSeperators("Time", "Seller", "Buyer", "price", "amountEnergy")+"\n");
            for (GatherData.TimedEnergyDeal ted : agentDeals) {
                writer.write(addSeperators(formatter.format(ted.time), ted.seller != null ? ted.seller.getLocalName() : "BigGuy", ted.buyer.getLocalName(), Double.toString(ted.price), Double.toString(ted.energyAmount)) + "\n");
            }
            writer.write("\n");
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
}
