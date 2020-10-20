package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.Branch;
import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 7.04.2019 at 23:34.
 */
public class CalculateAvailabilityRequest extends BaseRequest {
    public Branch dropoffBranch;
    public ContractInformation contractInformation;
}
