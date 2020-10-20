package com.creatifsoftware.rentgoservice.model;


import java.io.Serializable;

/**
 * Created by kerembalaban on 16.04.2019 at 16:32.
 */
public class EquipmentPart implements Serializable {
    public String equipmentPartId;
    public String equipmentPartMeshName;
    public String equipmentMainPartId;
    public String equipmentSubPartId;
    public String equipmentSubPartName;

    public boolean isSelected;
}
