package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetDamageListRequest extends BaseRequest {
    public String equipmentId;

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

}
