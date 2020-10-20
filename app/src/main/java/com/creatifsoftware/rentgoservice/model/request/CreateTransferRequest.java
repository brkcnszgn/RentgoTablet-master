package com.creatifsoftware.rentgoservice.model.request;

import com.creatifsoftware.rentgoservice.model.Branch;
import com.creatifsoftware.rentgoservice.model.base.BaseRequest;

/**
 * Created by kerembalaban on 25.04.2019 at 17:35.
 */
public class CreateTransferRequest extends BaseRequest {
    public Branch pickupBranch;
    public Branch dropoffBranch;
    public EquipmentInformation equipmentInformation;
    public UserInformation userInformation;
    public long estimatedPickupTimestamp;
    public long estimatedDropoffTimestamp;
    public String serviceName;
    public String description;
    public int transferType;
}
