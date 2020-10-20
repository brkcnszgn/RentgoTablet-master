package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 21.02.2019 at 01:11.
 */
public class GetCreditCardsRequest extends BaseRequest {
    public String customerId;
    public String contractId;

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
