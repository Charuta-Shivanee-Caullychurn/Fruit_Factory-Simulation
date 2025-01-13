/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */
// Van.java

import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Van {
    private final int vanCapacity = 18; 
    private final List<AtomicInteger> vans = new ArrayList<>();
    private int currentVanIndex = 0;
    private final Statistics statistics;
    private final Lock lock = new ReentrantLock();
    private final Condition vanAvailable = lock.newCondition();

    public Van(Statistics statistics) {
        this.statistics = statistics;
        for (int i = 0; i < 3; i++) {
            vans.add(new AtomicInteger(0)); // Initialize 3 vans with load 0
        }
    }

    // Load a box onto the next available van
    public void loadBox(int boxId) {
        lock.lock();
        try {
            boolean boxLoaded = false;
            long startTime = System.nanoTime();

            for (int i = 0; i < vans.size(); i++) {
                AtomicInteger currentVanLoad = vans.get(currentVanIndex);

                if (currentVanLoad.get() < vanCapacity) {
                    currentVanLoad.incrementAndGet();
                    
                    try {
                        Thread.sleep(100 + new Random().nextInt(100));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println(" Van loading interrupted!");
                    }
                    System.out.println( "\n//Van " + (currentVanIndex + 1) + " loading Box " + boxId + "\n");
                    boxLoaded = true;
                    vanAvailable.signalAll(); // Notify other threads that a van is available
                    break;
                } else {
                    currentVanIndex = (currentVanIndex + 1) % vans.size();
                }
            }

            if (!boxLoaded) {
                System.out.println("All vans are full. Box " + boxId + " will be delivered tomorrow.");
                statistics.incrementBoxesToBeDeliveredNextDay();
                resetVansForNextDay();
            } else {
                long endTime = System.nanoTime();
                long waitTimeMillis = ( endTime - startTime) / 1_000_000;
                if (waitTimeMillis > 0) {
                    statistics.recordVanWaitTime(waitTimeMillis);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // Reset vans' load for the next day
    private void resetVansForNextDay() {
        lock.lock();
        try {
            for (AtomicInteger vanLoad : vans) {
                vanLoad.set(0); // Clear each van's load for the next day
            }
            vanAvailable.signalAll(); // Notify any waiting threads
        } finally {
            lock.unlock();
        }
    }
}




