/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SterilisationSection implements Runnable {
    private final BlockingQueue<Can> goodCanQueue = new LinkedBlockingQueue<>();
    private final Statistics statistics;
    private final FillingSection fillingSection;
    private static final int BATCH_SIZE = 4;
    private static final Can END_MARKER = new Can(true);

    public SterilisationSection(Statistics statistics, FillingSection fillingSection) {
        this.statistics = statistics;
        this.fillingSection = fillingSection;
    }

    public void addGoodCan(Can can) {
        try {
            goodCanQueue.put(can);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addEndMarker() {
        try {
            goodCanQueue.put(END_MARKER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Can[] batch = new Can[BATCH_SIZE];
                for (int i = 0; i < BATCH_SIZE; i++) {
                    Can can = goodCanQueue.take();
                    if (can == END_MARKER) {
                        if (i > 0) {
                            System.out.println("\nBATCH OF " + i + " is less than 4 cans, not enough for sterilisation!");
                            statistics.incrementSterilizedBatches();
                        }
                        fillingSection.addEndMarker();
                        return;
                    }
                    batch[i] = can;
                }

                System.out.println("\nBATCH OF 4 CANS HAVE BEEN STERILISED NOW MOVING TO **FILLING STATION**\n");
                statistics.incrementSterilizedBatches();

                for (Can can : batch) {
                    fillingSection.addSterilizedCan(can);
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}




