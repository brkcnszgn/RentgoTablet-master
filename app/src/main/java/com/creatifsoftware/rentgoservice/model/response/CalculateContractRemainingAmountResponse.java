package com.creatifsoftware.rentgoservice.model.response;

import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:36.
 */
public class CalculateContractRemainingAmountResponse extends BaseResponse {
    public List<AdditionalProduct> otherAdditionalProductData;
    public CalculateAdditionalProductResponse additionalProductResponse;
    public CalculatePricesForUpdateContractResponse calculatePricesForUpdateContractResponse;
    public AdditionalProduct otherCostAdditionalProductData;
    public long dateNow;

}
