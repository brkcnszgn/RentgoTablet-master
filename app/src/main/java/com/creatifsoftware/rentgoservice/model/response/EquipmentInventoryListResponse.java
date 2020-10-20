package com.creatifsoftware.rentgoservice.model.response;

import com.creatifsoftware.rentgoservice.model.EquipmentInventory;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class EquipmentInventoryListResponse extends BaseResponse {
    public List<EquipmentInventory> equipmentInventoryData;
}
