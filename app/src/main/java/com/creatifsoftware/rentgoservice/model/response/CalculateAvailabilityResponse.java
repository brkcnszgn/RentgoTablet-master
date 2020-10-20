package com.creatifsoftware.rentgoservice.model.response;

import com.creatifsoftware.rentgoservice.model.AvailabilityGroupCodeInformation;
import com.creatifsoftware.rentgoservice.model.base.BaseResponse;

import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:36.
 */
public class CalculateAvailabilityResponse extends BaseResponse {
    public List<AvailabilityGroupCodeInformation> availabilityData;
    public String trackingNumber;

}
