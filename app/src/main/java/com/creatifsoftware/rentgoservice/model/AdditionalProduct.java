package com.creatifsoftware.rentgoservice.model;


import java.io.Serializable;

/**
 * Created by kerembalaban on 18.02.2019 at 23:10.
 */
public class AdditionalProduct implements Serializable {
    public String productId;
    public String productName;
    public int productType;
    public String productCode;
    public int maxPieces;
    public boolean showonWeb;
    public int webRank;
    public int billingType;
    public String productDescription;
    public double actualAmount;
    public boolean isChecked;
    //public boolean isFromReservation;
    public int value;
    public int priceCalculationType;
    public double actualTotalAmount;
    public double tobePaidAmount;
    public double monthlyPackagePrice;
    public boolean isMandatory;
    public boolean isUpdated;
    public boolean isServiceFee;

    public AdditionalProduct(AdditionalProduct additionalProduct) {
        this.productId = additionalProduct.productId;
        this.productName = additionalProduct.productName;
        this.productType = additionalProduct.productType;
        this.productCode = additionalProduct.productCode;
        this.maxPieces = additionalProduct.maxPieces;
        this.showonWeb = additionalProduct.showonWeb;
        this.webRank = additionalProduct.webRank;
        this.billingType = additionalProduct.billingType;
        this.productDescription = additionalProduct.productDescription;
        this.actualAmount = additionalProduct.actualAmount;
        this.tobePaidAmount = additionalProduct.tobePaidAmount;
        this.isChecked = additionalProduct.isChecked;
        //this.isFromReservation = additionalProduct.isFromReservation;
        this.value = additionalProduct.value;
        this.priceCalculationType = additionalProduct.priceCalculationType;
        this.actualTotalAmount = additionalProduct.actualTotalAmount;
        this.isMandatory = additionalProduct.isMandatory;
        this.monthlyPackagePrice = additionalProduct.monthlyPackagePrice;
        this.isUpdated = additionalProduct.isUpdated;
        this.isServiceFee = additionalProduct.isServiceFee;
    }
}
