package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:37.
 */
public class CalculateDamagesAmountResponse extends BaseResponse {
    public List<DamageItem> damageList;
    public AdditionalProduct damageProduct;
}
