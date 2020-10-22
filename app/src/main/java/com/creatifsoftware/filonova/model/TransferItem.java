package com.creatifsoftware.filonova.model;

import com.creatifsoftware.filonova.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by kerembalaban on 22.04.2019 at 17:08.
 */
public class TransferItem implements Serializable {
    public String transferId;
    public String transferNumber;
    public int transferType;
    public GroupCodeInformation groupCodeInformation;
    @SerializedName("pickupTimestamp")
    public long estimatedPickupTimestamp;
    @SerializedName("dropoffTimestamp")
    public long estimatedDropoffTimestamp;
    public Equipment selectedEquipment;
    public Branch pickupBranch;
    public Branch dropoffBranch;
    public String serviceName;
    public String description;
    public int statusCode;

    public String convertTimestampToDateTime(long timestamp) {
        return DateUtils.convertTimestampToStringDateTime(timestamp);
    }
}
