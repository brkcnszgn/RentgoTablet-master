package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.base.BaseRequest;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class UpdateTransferForReturnRequest extends BaseRequest {
    public String transferId;
    public EquipmentInformation equipmentInformation;
    public UserInformation userInformation;
    public List<DamageItem> damageList;
}

