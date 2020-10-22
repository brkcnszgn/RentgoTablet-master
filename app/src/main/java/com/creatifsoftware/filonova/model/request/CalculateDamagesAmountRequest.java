package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.DamageItem;
import com.creatifsoftware.filonova.model.base.BaseRequest;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:34.
 */
public class CalculateDamagesAmountRequest extends BaseRequest {
    public List<DamageItem> damageList;
    public String contractId;
}
