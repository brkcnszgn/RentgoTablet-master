package com.creatifsoftware.filonova.model;


import java.io.Serializable;

/**
 * Created by kerembalaban on 13.02.2019 at 00:47.
 */
public class AvailabilityGroupCodeInformation extends GroupCodeInformation implements Serializable {
    public double totalPrice;
    public double amountToBePaid;
    public boolean isUpgrade;
    public boolean isDowngrade;
    public boolean isPriceCalculatedSafely;
    public int changeType;
    public String trackingNumber;

    public AvailabilityGroupCodeInformation(AvailabilityGroupCodeInformation groupCodeInformation) {
        super(groupCodeInformation);

        this.totalPrice = groupCodeInformation.totalPrice;
        this.amountToBePaid = groupCodeInformation.amountToBePaid;
        this.isUpgrade = groupCodeInformation.isUpgrade;
        this.isDowngrade = groupCodeInformation.isDowngrade;
        this.isPriceCalculatedSafely = groupCodeInformation.isPriceCalculatedSafely;
        this.changeType = groupCodeInformation.changeType;
        this.trackingNumber = groupCodeInformation.trackingNumber;
    }
}


