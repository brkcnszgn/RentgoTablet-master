package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class UpdateContractForDeliveryRequest extends BaseRequest {

    public EquipmentInformation equipmentInformation;
    public UserInformation userInformation;
    public ContractInformation contractInformation;
    public PaymentInformation paymentInformation;
    public List<AdditionalProduct> additionalProducts;
    public List<DamageItem> damageData;
    public AvailabilityGroupCodeInformation changedEquipmentData;
    public String campaignId;
}