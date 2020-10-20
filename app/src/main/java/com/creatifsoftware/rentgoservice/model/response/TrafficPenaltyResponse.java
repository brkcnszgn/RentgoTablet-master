package com.creatifsoftware.rentgoservice.model.response;

import com.creatifsoftware.rentgoservice.model.AdditionalProduct;
import com.creatifsoftware.rentgoservice.model.TrafficPenaltyItem;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class TrafficPenaltyResponse extends BaseResponse {
    public List<TrafficPenaltyItem> fineList;
    public List<AdditionalProduct> fineAdditionalProducts;
}
