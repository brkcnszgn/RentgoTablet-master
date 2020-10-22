package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.base.BaseRequest;

/**
 * Created by kerembalaban on 17.02.2019 at 23:33.
 */
public class GetReservationListRequest extends BaseRequest {
    public String branchId;

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}