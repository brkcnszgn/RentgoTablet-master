package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.CreditCard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kerembalaban on 8.04.2019 at 20:35.
 */
public class PaymentInformation implements Serializable {
    public List<CreditCard> creditCardData;
}
