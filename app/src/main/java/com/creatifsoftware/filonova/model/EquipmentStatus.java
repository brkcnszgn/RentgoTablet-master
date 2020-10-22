package com.creatifsoftware.filonova.model;


import java.io.Serializable;

/**
 * Created by kerembalaban on 26.03.2019 at 22:49.
 */
public class EquipmentStatus implements Serializable {

    public int statusCodeId;
    public String statusCodeName;

    public enum StatusCode {
        AVAILABLE(1),
        IN_WASHING(100000002),
        IN_FUEL_FILLING(100000003),
        RENTAL(100000000),
        IN_SERVICE(5);

        private final int intValue;

        StatusCode(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }
}
