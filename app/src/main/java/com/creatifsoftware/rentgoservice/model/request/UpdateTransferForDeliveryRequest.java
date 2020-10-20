package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class UpdateTransferForDeliveryRequest extends BaseRequest {
    public String transferId;
    public EquipmentInformation equipmentInformation;
    public UserInformation userInformation;
    public List<DamageItem> damageList;
}