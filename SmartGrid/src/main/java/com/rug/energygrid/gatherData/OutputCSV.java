package com.rug.energygrid.gatherData;

import com.rug.energygrid.logging.LocalLogger;
import jade.core.AID;
import jade.util.Logger;

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

public class OutputCSV extends OutputData{
    private String fileName;
    private static final Logger logger = LocalLogger.getLogger();
    private static final char DEFAULT_SEPARATOR = ',';

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
                List<GatherData.TimedEnergyDeal> list = new ArrayList<>();
                list.add(ted);
                sellers.put(ted.seller, list);
            }
        }
    }

    @Override
    public void output(GatherData gatherData) {
        File file = createFile(fileName);
        PrintWriter writer = createWriter(file);
        writeDeals(writer, gatherData);
        writeProductions(writer, gatherData);
        System.out.println("Done with writing deals");
        logger.info("Done with writing deals");
        writer.close();
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

    private PrintWriter createWriter(File file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return writer;
    }

    private void writeDeals(PrintWriter writer, GatherData gatherData) {
        orderDeals(gatherData);
        for (List<GatherData.TimedEnergyDeal> agentDeals: sellers.values()) {
            writer.write("Seller: "+agentDeals.get(0).seller+"\n");
            writer.write(addSeperators("Time", "Seller", "Buyer", "price", "amountEnergy")+"\n");
            for (GatherData.TimedEnergyDeal ted : agentDeals) {
                try {
                    writer.write(addSeperators(formatter.format(ted.time),
                            ted.seller != null ? ted.seller.getLocalName() : "BigGuy",
                            ted.buyer.getLocalName(),
                            Double.toString(ted.price),
                            Double.toString(ted.energyAmount)) + "\n");
                } catch (NullPointerException e) {
                    logger.info("Our deal had an error writing");
                    System.out.println("" + ted.time + (ted.seller != null ? ted.seller.getLocalName() : "BigGuy") + ted.buyer + ted.price + ted.energyAmount);
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            writer.write("\n");
        }
    }

    private void writeProductions(PrintWriter writer, GatherData gatherData) {
        for (List<GatherData.TimedProduction> perAgentProductions: gatherData.getProductions().values()) {
            writer.write("Agent: "+perAgentProductions.get(0).producer.getLocalName()+"\n");
            writer.write(addSeperators("Time", "amount")+"\n");
            for (GatherData.TimedProduction tp : perAgentProductions) {
                writer.write(addSeperators(formatter.format(tp.time),Double.toString(tp.amount))+"\n");
            }
            writer.write("\n");
        }
    }


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
