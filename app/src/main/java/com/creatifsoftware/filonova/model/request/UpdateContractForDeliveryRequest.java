package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.base.BaseRequest;

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