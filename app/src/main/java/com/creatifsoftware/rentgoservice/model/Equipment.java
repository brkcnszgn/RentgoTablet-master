package com.creatifsoftware.rentgoservice.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerembalaban on 13.02.2019 at 00:49.
 */
public class Equipment implements Serializable {
    public String equipmentId;
    public String brandId;
    public String brandName;
    public String modelId;
    public String modelName;
    public String plateNumber;
    public String equipmentColor;
    public String seatNumber;
    public String doorNumber;
    public String luggageNumber;
    public File kilometerFuelImageFile;
    public String groupCodeId;
    public String hgsNumber;
    public int statusReason;
    public int fuelValue;
    public int kmValue;
    public int currentKmValue;
    public int currentFuelValue;
    public List<EquipmentInventory> inventoryList;
    public List<DamageItem> damageList;
    public List<HgsItem> transits = new ArrayList<>();
    public List<TrafficPenaltyItem> fineList = new ArrayList<>();
    public boolean isSelected = false;
}