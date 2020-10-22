package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetContractInformationRequest extends BaseRequest {
    public String contractId;
    public int statusCode;

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
