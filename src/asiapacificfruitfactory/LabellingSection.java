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

public class LabellingSection implements Runnable {
    private final BlockingQueue<Can> sealedCanQueue = new LinkedBlockingQueue<>();
    private final Statistics statistics;
    private final PackagingSection packagingSection;
    private static final double MISLABEL_DEFECT_RATE = 0.05;
    private static final Can END_MARKER = new Can(true);

    public LabellingSection(Statistics statistics, PackagingSection packagingSection) {
        this.statistics = statistics;
        this.packagingSection = packagingSection;
    }

    public void addSealedCan(Can can) {
        try {
            sealedCanQueue.put(can);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addEndMarker() {
        try {
            sealedCanQueue.put(END_MARKER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        Random random = new Random();

        try {
            while (true) {
                Can can = sealedCanQueue.take();
                if (can == END_MARKER) {
                    packagingSection.addEndMarker();
                    return;
                }

                System.out.println(">Can " + can.getId() + " is being labelled and Scanned");
                if (random.nextDouble() < MISLABEL_DEFECT_RATE) {
                    System.out.println("##Can " + can.getId() + " is mislabelled");
                    statistics.incrementMislabelledCans();
                } else {
                    System.out.println("##Can " + can.getId() + " is well labelled");
                    statistics.incrementWellLabelledCans();
                    packagingSection.addLabelledCan(can);
                }

                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
