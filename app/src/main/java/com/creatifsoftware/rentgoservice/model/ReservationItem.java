package com.creatifsoftware.rentgoservice.model;

import com.creatifsoftware.rentgoservice.model.base.BaseResponse;
import com.creatifsoftware.rentgoservice.utils.DateUtils;

import java.io.Serializable;

/**
 * Created by kerembalaban on 13.02.2019 at 00:45.
 */
public class ReservationItem extends BaseResponse implements Serializable {
    public String reservationId;
    public String reservationNumber;
    public String pnrNumber;
    public long pickupTimestamp;
    public long dropoffTimestamp;
    public Branch pickupBranch;
    public Branch dropoffBranch;
    public Customer customer;
    public GroupCodeInformation groupCodeInformation;
    public double depositAmount;
    public double reservationTotalAmount;
    public CreditCard paymentCard;

    public String convertTimestampToDate(long timestamp) {
        //long localeUTC = timestamp + TimeUnit.MINUTES.toMillis(180);
        return DateUtils.convertTimestampToStringDate(timestamp);
    }

    public String convertTimestampToTime(long timestamp) {
        //long localeUTC = timestamp + TimeUnit.MINUTES.toMillis(180);
        return DateUtils.convertTimestampToStringTime(timestamp);
    }

    public String convertTimestampToDateTime(long timestamp) {
        //long localeUTC = timestamp + TimeUnit.MINUTES.toMillis(180);
        return DateUtils.convertTimestampToStringDateTime(timestamp);
    }
}
