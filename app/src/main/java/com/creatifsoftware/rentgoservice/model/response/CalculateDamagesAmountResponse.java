package com.creatifsoftware.rentgoservice.model.response;

import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:37.
 */
public class CalculateDamagesAmountResponse extends BaseResponse {
    public List<DamageItem> damageList;
    public AdditionalProduct damageProduct;
}
