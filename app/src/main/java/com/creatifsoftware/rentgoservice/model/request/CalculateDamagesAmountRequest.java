package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:34.
 */
public class CalculateDamagesAmountRequest extends BaseRequest {
    public List<DamageItem> damageList;
    public String contractId;
}
