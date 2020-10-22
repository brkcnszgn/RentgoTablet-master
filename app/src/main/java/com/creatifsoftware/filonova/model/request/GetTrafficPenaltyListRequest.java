package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetTrafficPenaltyListRequest extends BaseRequest {
    public String plateNumber;
    public long pickupDateTimeStamp;
    public long dropoffDatetimeStamp;
    public boolean isManuelProcess;
}