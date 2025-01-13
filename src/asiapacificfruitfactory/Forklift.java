/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author charu
 */
// Forklift.java

public class Forklift implements Runnable {
    private final LoadingBay loadingBay;
    private final Van van;
    private final int id;
    private final Semaphore forkliftSemaphore;
    private final Random random = new Random();
    private int breakdownCount = 0; // Tracks the number of breakdowns
    private boolean isWaiting = false; // Tracks if the forklift is in a waiting state

    public Forklift(int id, LoadingBay loadingBay, Van van, Semaphore forkliftSemaphore) {
        this.id = id;
        this.loadingBay = loadingBay;
        this.van = van;
        this.forkliftSemaphore = forkliftSemaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Simulate occasional breakdown 
                if (breakdownCount < 2 && random.nextDouble() < 0.02) { // forklift have a 2% chance of breakdown
                    System.out.println("!!FORKLIFT " + id + " is experiencing a breakdown!!");
                    breakdownCount++;
                    Thread.sleep(3000); 
                    System.out.println("FORKLIFT " + id + " is back in operation.");
                    continue;
                }

                // Acquire forklift driver
                if (forkliftSemaphore.tryAcquire()) {
                    Integer boxId = null;

                    synchronized (loadingBay) {
                        // Wait if both bays are empty
                        while (loadingBay.isBay1Empty() && loadingBay.isBay2Empty()) {
                            if (!isWaiting) { // Log waiting message only once
                                //System.out.println("~~Forklift " + id + " found no boxes, waiting...");
                                isWaiting = true; // Mark as waiting
                            }
                            loadingBay.wait(); // Wait for notification from production
                        }

                        // Attempt to remove a box from the bays
                        boxId = loadingBay.removeBoxFromBay1();
                        if (boxId == null) {
                            boxId = loadingBay.removeBoxFromBay2();
                        }

                        // Notify production that space is now available
                        loadingBay.notifyAll();
                    }

                    // Reset waiting state when a box is found
                    isWaiting = false;

                    // Load box onto van if available
                    if (boxId != null) {
                        System.out.println("\n^^Forklift " + id + " picked up Box " + boxId+"\n");
                        van.loadBox(boxId);
                    }

                    forkliftSemaphore.release();
                }

                Thread.sleep(500); // Simulate time for next pick-up
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}








