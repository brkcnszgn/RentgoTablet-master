package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetContractByEquipmentRequest extends BaseRequest {
    public String plateNumber;

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}
