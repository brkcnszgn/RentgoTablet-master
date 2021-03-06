package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.HgsItem;
import com.creatifsoftware.rentgoservice.model.TrafficPenaltyItem;
import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class UpdateContractForRentalRequest extends BaseRequest {

    public EquipmentInformation equipmentInformation;
    public UserInformation userInformation;
    public ContractInformation contractInformation;
    public PaymentInformation paymentInformation;
    public List<DamageItem> damageData;
    public List<AdditionalProduct> additionalProducts;
    public List<AdditionalProduct> otherAdditionalProducts;
    public List<HgsItem> transits;
    public List<TrafficPenaltyItem> fineList;
    public String trackingNumber;
    public int operationType;
    public String campaignId;
    public int contractDeptStatus;
    public boolean canUserStillHasCampaignBenefit;
    public long dateNow;
}

