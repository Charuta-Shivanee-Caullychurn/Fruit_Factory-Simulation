/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author charu
 */
// Statistics.java



public class Statistics {
    private int totalCansEntered = 0;
    private int totalGoodCans = 0;
    private int totalDentedCans = 0;
    private int totalSterilizedBatches = 0;
    private int totalFilledCans = 0;
    private int totalUnderFilledCans = 0;
    private int totalSealedBatches = 0;
    private int totalNotProperlySealedCans = 0;
    private int totalWellLabelledCans = 0;
    private int totalMislabelledCans = 0;
    private int totalPacks = 0;
    private int totalBoxes = 0;
    private long totalBoxWaitTime = 0;
    private int boxWaitCount = 0;
    private long totalVanWaitTime = 0;
    private int vanWaitCount = 0;
    private long minVanWaitTime = Long.MAX_VALUE;
    private long maxVanWaitTime = Long.MIN_VALUE;
    private int boxesToBeDeliveredNextDay = 0;

    public synchronized void incrementTotalCansEntered() { 
        totalCansEntered++; 
    }
    
    public synchronized void incrementGoodCans() { 
        totalGoodCans++;
    }
    
    public synchronized void incrementDentedCans() { 
        totalDentedCans++; 
    }
    
    public synchronized void incrementSterilizedBatches() { 
        totalSterilizedBatches++; 
    }
    public synchronized void incrementFilledCans() {
        totalFilledCans++; 
    }
    
    public synchronized void incrementUnderFilledCans() { 
        totalUnderFilledCans++;
    }
    
    public synchronized void incrementSealedBatches() { 
        totalSealedBatches++;
    }
    
    public synchronized void incrementNotProperlySealedCans() { 
        totalNotProperlySealedCans++; 
    }
    
    public synchronized void incrementWellLabelledCans() {
        totalWellLabelledCans++; 
    }
    
    public synchronized void incrementMislabelledCans() {
        totalMislabelledCans++;
    }
    
    public synchronized void incrementPacks() {
        totalPacks++; 
    }
    
    public synchronized void incrementBoxes() {
        totalBoxes++; 
    }
    
    public synchronized void recordBoxWaitTime(long waitTime) {
        if (waitTime > 0) {
            totalBoxWaitTime += waitTime;
            boxWaitCount++;
        } else {
            System.err.println("Error! The time is not valid; its negative!");
        }
        
    }
    
     public synchronized void recordVanWaitTime(long waitTime) {
        if (waitTime > 0) {
        totalVanWaitTime += waitTime;
        vanWaitCount++;
        if (waitTime < minVanWaitTime) minVanWaitTime = waitTime;
        if (waitTime > maxVanWaitTime) maxVanWaitTime = waitTime;
    } else { 
            System.err.println("Error!");
        }}
     
       public synchronized void incrementBoxesToBeDeliveredNextDay() {
        boxesToBeDeliveredNextDay++;
    }

    public void printStatistics() {
        System.out.println("\n\n*****STATISTICS*****\n");
        System.out.println("Total cans entered: " + totalCansEntered);
        System.out.println("Total cans without dents: " + totalGoodCans);
        System.out.println("Total cans with dents: " + totalDentedCans);
        System.out.println("Total number of batches sterilised: " + totalSterilizedBatches);
        System.out.println("Total number of cans filled: " + totalFilledCans);
        System.out.println("Total number of cans under filled: " + totalUnderFilledCans);
        System.out.println("Total number of batches of cans sealed: " + totalSealedBatches);
        System.out.println("Total number of properly labelled cans: " + totalWellLabelledCans);
        System.out.println("Total number of mislabelled cans: " + totalMislabelledCans);
        System.out.println("Total number of batches packed: " + totalPacks);
        System.out.println("Total number of boxes: " + totalBoxes);
        System.out.println("Average waiting time in loading area:  " + (boxWaitCount == 0 ? 0 : totalBoxWaitTime / boxWaitCount ) + " seconds. ");
        System.out.println("Minimum van wait time: " + (vanWaitCount == 0 ? 0 : minVanWaitTime) + " seconds");
        System.out.println("Maximum van wait time: " + (vanWaitCount == 0 ? 0 : maxVanWaitTime )+ " seconds");
        System.out.println("Average van wait time: " + (vanWaitCount == 0 ? 0 : totalVanWaitTime / vanWaitCount) + " seconds");
        System.out.println("Total number of boxes to be delivered the next day: " + boxesToBeDeliveredNextDay);
    }
}
