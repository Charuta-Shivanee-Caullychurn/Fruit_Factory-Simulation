/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */
public class CansEnteringSection implements Runnable {
    
    private static final int TOTAL_CANS = 600;
    private static final double DENT_DEFECT_RATE = 0.05;
    private final RejectGrabber rejectGrabber;
    private final SterilisationSection sterilisationSection;
    private final Statistics statistics;

    public CansEnteringSection(RejectGrabber rejectGrabber, SterilisationSection sterilisationSection, Statistics statistics) {
        this.rejectGrabber = rejectGrabber;
        this.sterilisationSection = sterilisationSection;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        for (int i = 0; i < TOTAL_CANS; i++) {
            Can can = new Can();
            statistics.incrementTotalCansEntered();

            System.out.println("Can " + can.getId() + " entered the factory and is being scanned.");

            if (Math.random() < DENT_DEFECT_RATE) {
                System.out.println("--Can " + can.getId() + " has a DENT and is moving to REJECT GRABBER--");
                statistics.incrementDentedCans();
                rejectGrabber.addRejectedCan(can);
            } else {
                System.out.println("--Can " + can.getId() + " is good--");
                statistics.incrementGoodCans();
                sterilisationSection.addGoodCan(can);
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}









