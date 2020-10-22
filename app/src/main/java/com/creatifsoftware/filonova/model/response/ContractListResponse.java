package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.ContractItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 17.02.2019 at 19:47.
 */
public class ContractListResponse extends BaseResponse {
    public List<ContractItem> deliveryContractList;
    public List<ContractItem> rentalContractList;
}
