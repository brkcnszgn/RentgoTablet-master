package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.EquipmentInventory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kerembalaban on 21.02.2019 at 03:17.
 */
public class EquipmentInformation implements Serializable {
    public String equipmentId;
    public String plateNumber;
    public int currentKmValue;
    public String groupCodeInformationId;
    public int firstKmValue;
    public int currentFuelValue;
    public int firstFuelValue;
    public List<EquipmentInventory> equipmentInventoryData;
    public boolean isEquipmentChanged;
}
