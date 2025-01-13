/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asiapacificfruitfactory;

/**
 *
 * @author charu
 */
// Can.java

import java.util.Random;


public class Can {
    // Unique ID for each can
    private static int nextId = 1;
    private final int id;
    private final DefectType defectType;

    // Enum for different defect types
    public enum DefectType {
        NONE, DENTED, MISLABELLED, UNDER_FILLED, IMPROPERLY_SEALED
    }

    // Constructor for regular cans
    public Can() {
        this.id = nextId++;
        this.defectType = generateRandomDefect();
    }

    // Constructor for END_MARKER can with fixed ID and no defect
    public Can(boolean isEndMarker) {
        if (isEndMarker) {
            this.id = 0; // Assign ID 0 for end marker to avoid incrementing `nextId`
            this.defectType = DefectType.NONE;
        } else {
            this.id = nextId++;
            this.defectType = generateRandomDefect();
        }
    }

    // Generates a random defect based on probability (80% no defect, 20% chance of defect)
    private DefectType generateRandomDefect() {
        Random random = new Random();
        int defectChance = random.nextInt(100);
        if (defectChance < 80) {
            return DefectType.NONE;
        } else {
            DefectType[] defects = {DefectType.DENTED, DefectType.MISLABELLED,
                                    DefectType.UNDER_FILLED, DefectType.IMPROPERLY_SEALED};
            return defects[random.nextInt(defects.length)];
        }
    }

    public int getId() {
        return id;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public boolean hasDefect() {
        return defectType != DefectType.NONE;
    }

    public String getDefectDescription() {
        if (hasDefect()) {
            return "Defect: " + defectType.toString();
        } else {
            return "No Defect";
        }
    }
}







