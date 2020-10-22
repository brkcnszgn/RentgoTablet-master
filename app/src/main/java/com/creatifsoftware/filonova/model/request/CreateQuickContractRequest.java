package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.base.BaseRequest;

/**
 * Created by kerembalaban on 2019-05-24 at 11:20.
 */
public class CreateQuickContractRequest extends BaseRequest {
    public UserInformation userInformation;
    public QuickContractCustomerParameter customerInformation;
    public QuickContractPriceInformation priceInformation;
    public String reservationId;
    public String reservationPNR;
}
