package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetAdditionalProductListRequest extends BaseRequest {
    public String customerId;
    public String contractId;
    public String groupCodeId;

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setGroupCodeId(String groupCodeId) {
        this.groupCodeId = groupCodeId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
