/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SealingSection implements Runnable {
    private final BlockingQueue<Can> filledCanQueue = new LinkedBlockingQueue<>();
    private final Statistics statistics;
    private final LabellingSection labellingSection;
    private static final int BATCH_SIZE = 12;
    private static final double SEAL_DEFECT_RATE = 0.05;
    private static final Can END_MARKER = new Can(true);

    public SealingSection(Statistics statistics, LabellingSection labellingSection) {
        this.statistics = statistics;
        this.labellingSection = labellingSection;
    }

    public void addFilledCan(Can can) {
        try {
            filledCanQueue.put(can);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addEndMarker() {
        try {
            filledCanQueue.put(END_MARKER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        Random random = new Random();

        try {
            while (true) {
                Can[] batch = new Can[BATCH_SIZE];
                for (int i = 0; i < BATCH_SIZE; i++) {
                    Can can = filledCanQueue.take();
                    if (can == END_MARKER) {
                        if (i > 0) {
                            System.out.println("!BATCH OF " + i + " CANNOT BE SEALED, SINCE ITS < 12!");
                            statistics.incrementSealedBatches();
                        }
                        labellingSection.addEndMarker();
                        return;
                    }
                    batch[i] = can;
                }

                System.out.println("\nBATCH OF 12 CANS HAVE BEEN SEALED\n");
                statistics.incrementSealedBatches();

                for (Can can : batch) {
                    if (random.nextDouble() < SEAL_DEFECT_RATE) {
                        System.out.println("--Can " + can.getId() + " is NOT PROPERLY SEALED--");
                        statistics.incrementNotProperlySealedCans();
                    } else {
                        System.out.println("--Can " + can.getId() + " is PROPERLY SEALED NOW MOVING TO **LABELLING SECTION**--");
                        labellingSection.addSealedCan(can);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
   

