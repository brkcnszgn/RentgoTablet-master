package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:36.
 */
public class CalculateAvailabilityResponse extends BaseResponse {
    public List<AvailabilityGroupCodeInformation> availabilityData;
    public String trackingNumber;

}
