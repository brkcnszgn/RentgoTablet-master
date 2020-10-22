package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetHgsTransitListRequest extends BaseRequest {
    public String productId;
    public long startDateTimeStamp;
    public long finishDateTimeStamp;
    public boolean isManuelProcess;
}
