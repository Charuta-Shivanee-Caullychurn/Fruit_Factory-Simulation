/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author charu
 */

public class PackagingSection implements Runnable {
    private final BlockingQueue<Can> labelledCanQueue = new LinkedBlockingQueue<>();
    private final Statistics statistics;
    private final LoadingBay loadingBay;
    private static final int PACK_SIZE = 6;
    private static final int BOX_SIZE = 2;
    private static final Can END_MARKER = new Can(true);
    private final Semaphore packagingSemaphore = new Semaphore(1);
    private int packsInCurrentBox = 0;
    private int boxCounter = 1;

    public PackagingSection(Statistics statistics, LoadingBay loadingBay) {
        this.statistics = statistics;
        this.loadingBay = loadingBay;
    }

    public void addLabelledCan(Can can) {
        try {
            labelledCanQueue.put(can);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addEndMarker() {
        try {
            labelledCanQueue.put(END_MARKER);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Can[] pack = new Can[PACK_SIZE];
                for (int i = 0; i < PACK_SIZE; i++) {
                    Can can = labelledCanQueue.take();
                    if (can == END_MARKER) {
                        if (i > 0) {
                            packagePack(pack, i);
                        }
                        loadingBay.addBox(LoadingBay.END_MARKER); // Send end marker to LoadingBay
                        return;
                    }
                    pack[i] = can;
                }
                packagePack(pack, PACK_SIZE);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void packagePack(Can[] pack, int size) throws InterruptedException {
        packagingSemaphore.acquire();
        try {
            System.out.println("\nBATCH OF " + size + " CANS HAVE BEEN PACKAGED NOW MOVING TO **BOX**\n");
            statistics.incrementPacks();

            packsInCurrentBox++;
            if (packsInCurrentBox == BOX_SIZE) {
                System.out.println("\nBOX HAS BEEN FILLED WITH 2 PACKS\n");
                statistics.incrementBoxes();
                loadingBay.addBox(boxCounter++);
                packsInCurrentBox = 0;
            }
            Thread.sleep(500);
        } finally {
            packagingSemaphore.release();
        }
    }
}

