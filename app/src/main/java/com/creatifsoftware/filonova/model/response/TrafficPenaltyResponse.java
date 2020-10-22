package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.AdditionalProduct;
import com.creatifsoftware.filonova.model.TrafficPenaltyItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class TrafficPenaltyResponse extends BaseResponse {
    public List<TrafficPenaltyItem> fineList;
    public List<AdditionalProduct> fineAdditionalProducts;
}
