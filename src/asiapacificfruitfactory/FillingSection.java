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

public class FillingSection implements Runnable {
    private final BlockingQueue<Can> sterilizedCanQueue = new LinkedBlockingQueue<>();
    private final RejectGrabber rejectGrabber;
    private final Statistics statistics;
    private final SealingSection sealingSection;
    private static final double UNDER_FILL_DEFECT_RATE = 0.05;
    private static final Can END_MARKER = new Can(true);

    public FillingSection(RejectGrabber rejectGrabber, Statistics statistics, SealingSection sealingSection) {
        this.rejectGrabber = rejectGrabber;
        this.statistics = statistics;
        this.sealingSection = sealingSection;
    }

    public void addSterilizedCan(Can can) {
        try {
            sterilizedCanQueue.put(can);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addEndMarker() {
        try {
            sterilizedCanQueue.put(END_MARKER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            while (true) {
                Can can = sterilizedCanQueue.take();
                if (can == END_MARKER) {
                    sealingSection.addEndMarker();
                    return;
                }

                System.out.println("#Can " + can.getId() + " is being filled and scanned");
                statistics.incrementFilledCans();

                if (random.nextDouble() < UNDER_FILL_DEFECT_RATE) {
                    System.out.println("##Can " + can.getId() + " is under_filled and is moving to REJECT GRABBER");
                    statistics.incrementUnderFilledCans();
                    rejectGrabber.addRejectedCan(can);
                } else {
                    System.out.println("##Can " + can.getId() + " is good and now moving to **SEALING SECTION**");
                    sealingSection.addFilledCan(can);
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

