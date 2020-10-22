package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.AdditionalProductRules;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class AdditionalProductListResponse extends BaseResponse {
    public List<AdditionalProduct> additionalProductData;
    public List<AdditionalProductRules> additionalProductRuleData;
}
