/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */
// RejectGrabber.java
// RejectGrabber.java

import java.util.concurrent.atomic.AtomicInteger;

public class RejectGrabber {
    private final AtomicInteger dentedCanCount = new AtomicInteger(0);

    public void addRejectedCan(Can can) {
        if (can.getDefectType() == Can.DefectType.DENTED) {
            dentedCanCount.incrementAndGet();
        }
    }

    public int getDentedCanCount() {
        return dentedCanCount.get();
    }
}





    
