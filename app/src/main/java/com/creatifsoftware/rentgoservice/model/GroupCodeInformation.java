package com.creatifsoftware.rentgoservice.model;

import com.creatifsoftware.rentgoservice.utils.CommonMethods;

import java.io.Serializable;

/**
 * Created by kerembalaban on 13.02.2019 at 00:47.
 */
public class GroupCodeInformation implements Serializable {
    public String groupCodeId;
    public String pricingGroupCodeId;
    public String groupCodeName;
    public String displayText;
    public String groupCodeDescription;
    public String transmissionName;
    public String fuelTypeName;
    public String groupCodeImage;
    public boolean isDoubleCard;
    public double depositAmount = 0;
    //public List<Equipment> equipmentList;

    public GroupCodeInformation(GroupCodeInformation groupCodeInformation) {
        this.groupCodeId = groupCodeInformation == null ? "" : groupCodeInformation.groupCodeId;
        this.pricingGroupCodeId = groupCodeInformation == null ? "" : groupCodeInformation.pricingGroupCodeId;
        this.groupCodeName = groupCodeInformation == null ? "" : groupCodeInformation.groupCodeName;
        this.displayText = groupCodeInformation == null ? "" : groupCodeInformation.displayText;
        this.groupCodeDescription = groupCodeInformation == null ? "" : groupCodeInformation.groupCodeDescription;
        this.transmissionName = groupCodeInformation == null ? "" : groupCodeInformation.transmissionName;
        this.fuelTypeName = groupCodeInformation == null ? "" : groupCodeInformation.fuelTypeName;
        this.groupCodeImage = groupCodeInformation == null ? "" : groupCodeInformation.groupCodeImage;
        this.isDoubleCard = groupCodeInformation != null && groupCodeInformation.isDoubleCard;
        this.depositAmount = groupCodeInformation == null ? 0.0 : groupCodeInformation.depositAmount;
    }

    public String getGroupCodeImageById(String groupCodeId) {
        return CommonMethods.instance.getSelectedGroupCodeInformation(groupCodeId).groupCodeImage;
    }
}
