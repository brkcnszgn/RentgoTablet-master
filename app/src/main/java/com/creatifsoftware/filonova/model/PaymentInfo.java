package com.creatifsoftware.filonova.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by kerembalaban on 2019-05-13 at 17:46.
 */
public class PaymentInfo implements Serializable {
    public String paymentId;
    public int value;
    public String paymentInfoTitle;
    public double paymentInfoAmount;

    public PaymentInfo(String paymentInfoTitle, double paymentInfoAmount, int value) {
        this.paymentId = UUID.randomUUID().toString();
        this.paymentInfoTitle = paymentInfoTitle;
        this.paymentInfoAmount = paymentInfoAmount;
        this.value = value;
    }
}
