package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.Branch;
import com.creatifsoftware.filonova.model.base.BaseRequest;

/**
 * Created by kerembalaban on 7.04.2019 at 23:34.
 */
public class CalculateAvailabilityRequest extends BaseRequest {
    public Branch dropoffBranch;
    public ContractInformation contractInformation;
}
