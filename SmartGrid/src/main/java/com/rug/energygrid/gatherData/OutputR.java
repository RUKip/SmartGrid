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
public class OutputR extends OutputData{
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone( ZoneId.systemDefault() );
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone( ZoneId.systemDefault() );

    private static final String OUTPUT_FILE_NAME = "output";
    private static final String PRODUCTION_FILE_NAME = "production.csv";
    private static final String ENERGYSTATUS_FILE_NAME = "energyStatus.csv";
    private static final String SELLERDEALS_FILE_NAME = "sellerDeals.csv";
    private static final String BUYERDEALS_FILE_NAME = "buyerDeals.csv";

    HashMap<AID, List<GatherData.TimedEnergyDeal>> sellers = new HashMap<>();
    HashMap<AID, List<GatherData.TimedEnergyDeal>> buyers = new HashMap<>();

    @Override
    public void output(GatherData gatherData) {
        File outputFolder = new File(OUTPUT_FILE_NAME);
        outputFolder.mkdir();
        orderDeals(gatherData);
        for (AID agent : gatherData.getAgents()) {
            File localFolder = new File(outputFolder, agent.getLocalName());
            localFolder.mkdir();
            storeAgentData(agent, localFolder, gatherData);
        }
        System.out.println("Done with writing deals");


        /*File file = createFile(fileName);
        File production = createFile("production.csv");
        PrintWriter writer = createWriter(file);
        writeDeals(writer, gatherData);
        PrintWriter writerProduction = createWriter(production);
        writeProductions(writerProduction, gatherData);
        System.out.println("Done with writing deals");
        writer.close();
        writerProduction.close();*/
    }

    private void storeAgentData(AID agent, File localFolder, GatherData gatherData) {
        storeProduction(agent, localFolder, gatherData);
        storeEnergyStatus(agent, localFolder, gatherData);
        storeDeals(agent, localFolder, gatherData);
    }

    private void storeProduction(AID agent, File localFolder, GatherData gatherData) {
        File productionFile = createFile(localFolder, PRODUCTION_FILE_NAME);
        PrintWriter writer = createWriter(productionFile);
        writer.write(addSeperators("Date","Time", "amount") + "\n");
        for (GatherData.TimedProduction tp : gatherData.getProductions().get(agent)) {
            writer.write(addSeperators(dateFormatter.format(tp.time), timeFormatter.format(tp.time),Double.toString(tp.amount))+"\n");
        }
        writer.close();
    }

    private void storeEnergyStatus(AID agent, File localFolder, GatherData gatherData) {
        File energyStatusFile = createFile(localFolder, ENERGYSTATUS_FILE_NAME);
        PrintWriter writer = createWriter(energyStatusFile);
        writer.write(addSeperators("Date","Time", "amount") + "\n");
        for (GatherData.TimedProduction tp : gatherData.getEnergyStatus().get(agent)) {
            writer.write(addSeperators(dateFormatter.format(tp.time), timeFormatter.format(tp.time),Double.toString(tp.amount))+"\n");
        }
        writer.close();
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


        for (GatherData.TimedEnergyDeal ted: gatherData.getDeals()) {
            if (sellers.containsKey(ted.buyer)) {
                buyers.get(ted.buyer).add(ted);
            } else {
                List<GatherData.TimedEnergyDeal> list = new ArrayList<>();
                list.add(ted);
                buyers.put(ted.buyer, list);
            }
        }
    }

    private void storeSellerDeals(AID agent, File localFolder, GatherData gatherData) {
        File sellerDeals = createFile(localFolder, SELLERDEALS_FILE_NAME);
        PrintWriter writer = createWriter(sellerDeals);
        writer.write(addSeperators("Date", "Time", "Buyer", "price", "amount")+"\n");
        for (GatherData.TimedEnergyDeal ted : sellers.get(agent)) {
            writer.write(addSeperators(dateFormatter.format(ted.time),
                    timeFormatter.format(ted.time),
                    ted.buyer.getLocalName(),
                    Double.toString(ted.price),
                    Double.toString(ted.energyAmount)) + "\n");
        }
        writer.close();
    }

    private void storeBuyerDeals(AID agent, File localFolder, GatherData gatherData) {
        File buyerDeals = createFile(localFolder, BUYERDEALS_FILE_NAME);
        PrintWriter writer = createWriter(buyerDeals);
        writer.write(addSeperators("Date", "Time", "Seller", "price", "amount")+"\n");
        for (GatherData.TimedEnergyDeal ted : buyers.get(agent)) {
            writer.write(addSeperators(dateFormatter.format(ted.time),
                    timeFormatter.format(ted.time),
                    ted.seller.getLocalName(),
                    Double.toString(ted.price),
                    Double.toString(ted.energyAmount)) + "\n");
        }
        writer.close();
    }

    private void storeDeals(AID agent, File localFolder, GatherData gatherData) {
        orderDeals(gatherData);
        storeSellerDeals(agent, localFolder, gatherData);
        storeBuyerDeals(agent, localFolder, gatherData);
    }

    public File createFile(File parentFolder, String name) {
        File csvFile = new File(parentFolder, name);
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
        boolean first = false;
        for (List<GatherData.TimedEnergyDeal> agentDeals: sellers.values()) {
            if (!first) {
                writer.write(addSeperators("Date", "Time", "Seller", "Buyer", "price", "amountEnergy") + "\n");
                for (GatherData.TimedEnergyDeal ted : agentDeals) {
                    try {
                        writer.write(addSeperators(dateFormatter.format(ted.time),
                                timeFormatter.format(ted.time),
                                ted.seller != null ? ted.seller.getLocalName() : "BigGuy",
                                ted.buyer.getLocalName(),
                                Double.toString(ted.price),
                                Double.toString(ted.energyAmount)) + "\n");
                    } catch (NullPointerException e) {
                        System.out.println("" + ted.time + (ted.seller != null ? ted.seller.getLocalName() : "BigGuy") + ted.buyer + ted.price + ted.energyAmount);
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
                first = true;
            }
            //writer.write("\n");
        }
    }

    private void writeProductions(PrintWriter writer, GatherData gatherData) {
        for (List<GatherData.TimedProduction> perAgentProductions: gatherData.getProductions().values()) {
            if (perAgentProductions.get(0).producer.getLocalName().equals("9471KN24")) {
                //writer.write("Agent: " + perAgentProductions.get(0).producer.getLocalName() + "\n");
                writer.write(addSeperators("Date","Time", "amount") + "\n");
                for (GatherData.TimedProduction tp : perAgentProductions) {
                    writer.write(addSeperators(dateFormatter.format(tp.time), timeFormatter.format(tp.time),Double.toString(tp.amount))+"\n");
                }
                //writer.write("\n");
            }
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
