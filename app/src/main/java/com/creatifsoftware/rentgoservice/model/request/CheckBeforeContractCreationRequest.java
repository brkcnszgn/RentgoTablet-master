package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 2019-05-28 at 17:04.
 */
public class CheckBeforeContractCreationRequest extends BaseRequest {
    public String reservationId;
    public boolean isQuickContract;
    public long pickupDateTimeStamp;
    public String contactId;

}
