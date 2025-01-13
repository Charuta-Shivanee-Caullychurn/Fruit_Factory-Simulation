/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package asiapacificfruitfactory;



/**
 *
 * @author charu
 */



//**********************************************************************************************************************************************
//                              NAME: CHARUTA SHIVANEE CAULLYCHURN
//                              TP:TP070567
//                              ASIA PACIFIC FRUIT FACTORY SIMULATION USING CONCURRENCY CONCEPT.
//**********************************************************************************************************************************************


import java.util.concurrent.Semaphore;

public class AsiaPacificFruitFactory {
    public static void main(String[] args) {
        
        System.out.println("Welcome to Asia Pacific Fruit Factory! Let's get to work!\n\n");
        
        
        // Initialize 
        Statistics statistics = new Statistics();
        
        LoadingBay loadingBay = new LoadingBay(statistics);
        Van van = new Van(statistics);
        Semaphore forkliftSemaphore = new Semaphore(3); // 3 forklifts available for loading vans
        RejectGrabber rejectGrabber = new RejectGrabber();

        // Initialize production sections and pass dependencies between them
        PackagingSection packagingSection = new PackagingSection(statistics, loadingBay);
        LabellingSection labellingSection = new LabellingSection(statistics, packagingSection);
        SealingSection sealingSection = new SealingSection(statistics, labellingSection);
        FillingSection fillingSection = new FillingSection(rejectGrabber, statistics, sealingSection);
        SterilisationSection sterilisationSection = new SterilisationSection(statistics, fillingSection);

        // Start threads for each production stage
        Thread sterilisationThread = new Thread(sterilisationSection);
        Thread fillingThread = new Thread(fillingSection);
        Thread sealingThread = new Thread(sealingSection);
        Thread labellingThread = new Thread(labellingSection);
        Thread packagingThread = new Thread(packagingSection);
        Thread loadingBayThread = new Thread(loadingBay); 

        // Forklift threads to move boxes from loading bay to vans
        Thread[] forkliftThreads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            forkliftThreads[i] = new Thread(new Forklift(i + 1, loadingBay, van, forkliftSemaphore));
            forkliftThreads[i].start();
        }

        // Start the loading bay thread, which  frees up some space
        loadingBayThread.start();

        // Start each production stage thread
        sterilisationThread.start();
        fillingThread.start();
        sealingThread.start();
        labellingThread.start();
        packagingThread.start();

        // Start the entry section where cans are scanned and defects are handled
        CansEnteringSection cansEnteringSection = new CansEnteringSection(rejectGrabber, sterilisationSection, statistics);
        Thread cansEntryThread = new Thread(cansEnteringSection);
        cansEntryThread.start();

        try {
            // Wait for the cans entry section to complete
            cansEntryThread.join();

            // Signal the end of each production stage using end markers
            sterilisationSection.addEndMarker();
            sterilisationThread.join();
            
            fillingSection.addEndMarker();
            fillingThread.join();
            
            sealingSection.addEndMarker();
            sealingThread.join();
            
            labellingSection.addEndMarker();
            labellingThread.join();
            
            packagingSection.addEndMarker();
            packagingThread.join();
            
            // Signal the end of the loading bay thread by adding the end marker
            loadingBay.addBox(LoadingBay.END_MARKER);
            loadingBayThread.join();
            
            // Wait for all forklift threads to finish
            for (Thread forkliftThread : forkliftThreads) {
                forkliftThread.interrupt();
                forkliftThread.join();
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Display final statistics
        System.out.println("\n\n FACTORY OPERATION SIMULATION COMPLETE YAY!");
        statistics.printStatistics();
    }
}







