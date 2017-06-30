package com.rug.energygrid.gatherData;

import com.rug.energygrid.logging.LocalLogger;
import jade.util.Logger;

public class OutputBigGuy extends OutputData{
    private Logger logger = LocalLogger.getLogger();
    private double sold = 0.0;
    private double notBigGuySold = 0.0;
    private double bought = 0.0;
    private double notBigGuyBought = 0.0;
    private double totalSold = 0.0;


    @Override
    public void output(GatherData gatherData) {
        System.out.println("Size: "+gatherData.getDeals().size());
        for (GatherData.TimedEnergyDeal deal : gatherData.getDeals()) {
            if (deal.seller.getLocalName().equals("BigGuyEssent")) {
                sold += deal.energyAmount;
            } else {
                notBigGuySold += deal.energyAmount;
            }
            if (deal.buyer.getLocalName().equals("BigGuyEssent")) {
                bought += deal.energyAmount;
            } else {
                notBigGuyBought+= deal.energyAmount;
            }
            totalSold += deal.energyAmount;
        }
        logger.info("bigGuy sold: "+ sold+ ", bought: "+bought+ " totalSold: "+totalSold + " not Big guy sold: "+notBigGuySold+ " not big guy Bought: "+notBigGuyBought);
        System.out.println(sold+" : " +bought +" , " + notBigGuySold+" : "+notBigGuyBought);
    }
}
