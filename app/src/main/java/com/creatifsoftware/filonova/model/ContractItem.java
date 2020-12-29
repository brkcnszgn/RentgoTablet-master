package com.creatifsoftware.filonova.model;

import com.creatifsoftware.filonova.model.base.BaseResponse;
import com.creatifsoftware.filonova.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerembalaban on 13.02.2019 at 00:45.
 */
public class ContractItem extends BaseResponse implements Serializable {
    public String contractId;
    public String contractNumber;
    public String pnrNumber;
    public long pickupTimestamp;
    public long dropoffTimestamp;
    public long manuelPickupDateTimeStamp;
    public long manuelDropoffTimeStamp;
    public Branch pickupBranch;
    public Branch dropoffBranch;
    public Customer customer;
    public GroupCodeInformation groupCodeInformation;
    public AvailabilityGroupCodeInformation updatedGroupCodeInformation;
    public Equipment rentalEquipment;
    public Equipment selectedEquipment;
    public List<AdditionalProduct> extraPaymentList;
    public List<AdditionalProduct> additionalProductList;
    public List<AdditionalProduct> addedAdditionalProducts;
    public List<AdditionalProduct> addedAdditionalServices;
    public List<AdditionalProductRules> additionalProductRules;
    public List<AdditionalProduct> initialAdditionalProductList = new ArrayList<>();
    public boolean isEquipmentChanged = false;
    public boolean isManuelProcess;
    public double carDifferenceAmount = 0;
    public String trackingNumber;
    public int operationType;
    public boolean canUserStillHasCampaignBenefit;
    public String campaignId;
    public ArrayList<CreditCard> creditCards;
    public double depositAmount = 0;
    public boolean hasPaymentError = false;
    public long dateNow;
    public boolean hasAdditionalDriver;
    public List<String> additionalDrivers = new ArrayList<>();

    public String convertTimestampToDate(long timestamp) {
        //long localeUTC = timestamp + TimeUnit.MINUTES.toMillis(180);
        return DateUtils.convertTimestampToStringDate(timestamp);
    }

    public String convertTimestampToTime(long timestamp) {
        //long localeUTC = timestamp + TimeUnit.MINUTES.toMillis(180);
        return DateUtils.convertTimestampToStringTime(timestamp);
    }

    public String convertTimestampToDateTime(long timestamp) {
        //long localeUTC = timestamp + TimeUnit.MINUTES.toMillis(180);
        return DateUtils.convertTimestampToStringDateTime(timestamp);
    }
}
