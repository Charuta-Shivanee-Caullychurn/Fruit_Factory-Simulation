/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class LoadingBay implements Runnable {
    public static final int END_MARKER = -1;
    private final Statistics statistics;
    private final Queue<Integer> bay1Queue = new LinkedList<>();
    private final Queue<Integer> bay2Queue = new LinkedList<>();
    private final Semaphore bay1Capacity = new Semaphore(10); 
    private final Semaphore bay2Capacity = new Semaphore(10); 
    private boolean useLoadingBay1 = true;
    private volatile boolean running = true; 

    public LoadingBay(Statistics statistics) {
        this.statistics = statistics;
    }

    // Adds a box to one of the loading bays
    public synchronized void addBox(int boxId) {
        if (boxId == END_MARKER) {
            running = false;
            notifyAll(); // Notify all threads to finish their work
            return;
        }

        try {
            // Wait if both bays are full
            while (bay1Queue.size() >= 10 && bay2Queue.size() >= 10) {
                System.out.println("\\ LOADING BAYS FULL, PAUSING PRODUCTION \\");
                wait(); // Wait until forklifts make space
            }

            // Fill Loading Bay 1 if space is available
            if (useLoadingBay1 && bay1Queue.size() < 10) {
                bay1Queue.add(boxId);
                bay1Capacity.acquire(); // Decrement capacity
                System.out.println("\n~~ Box " + boxId + " moving to Loading Bay 1 ~~\n");
            }
            // Fill Loading Bay 2 if space is available
            else  if (bay2Queue.size()< 10 ){
                bay2Queue.add(boxId);
                bay2Capacity.acquire(); // Decrement capacity
                System.out.println("\n~~ Box " + boxId + " moving to Loading Bay 2 ~~\n");
            }
            
            useLoadingBay1 = !useLoadingBay1;

            // Notify forklifts that boxes are available
            notifyAll();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while adding box " + boxId);
        }
    }

    // Removes a box from Loading Bay 1
    public synchronized Integer removeBoxFromBay1() {
        if (!bay1Queue.isEmpty()) {
            int boxId = bay1Queue.poll();
            bay1Capacity.release(); // Free up space in Bay 1
            return boxId;
        }
        return null; // Bay 1 is empty
    }

    // Removes a box from Loading Bay 2
    public synchronized Integer removeBoxFromBay2() {
        if (!bay2Queue.isEmpty()) {
            int boxId = bay2Queue.poll();
            bay2Capacity.release(); // Free up space in Bay 2
            return boxId;
        }
        return null; // Bay 2 is empty
    }

    // special method to check if Bay 1 is empty
    public synchronized boolean isBay1Empty() {
        return bay1Queue.isEmpty();
    }

    // special method to check if Bay 2 is empty
    public synchronized boolean isBay2Empty() {
        return bay2Queue.isEmpty();
    }

    // Run method for the Loading Bay thread
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(2000); // Simulate periodic monitoring
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}






