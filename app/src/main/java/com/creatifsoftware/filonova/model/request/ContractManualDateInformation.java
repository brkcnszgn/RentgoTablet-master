package com.creatifsoftware.filonova.model.request;

import java.io.Serializable;

/**
 * Created by kerembalaban on 21.02.2019 at 03:17.
 */
public class ContractManualDateInformation implements Serializable {
    public String contractId;
    public String contactId;
    public long manuelPickupDateTimeStamp;
    public long manuelDropoffTimeStamp;
    public long PickupDateTimeStamp;
    public long DropoffTimeStamp;
    public String isManuelProcess;

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
