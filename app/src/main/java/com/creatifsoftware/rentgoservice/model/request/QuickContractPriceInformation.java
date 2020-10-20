package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.CreditCard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kerembalaban on 2019-05-24 at 11:20.
 */
public class QuickContractPriceInformation implements Serializable {
    public List<CreditCard> creditCardData;
}
