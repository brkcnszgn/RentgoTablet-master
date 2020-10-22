package com.creatifsoftware.filonova.model.request;

import com.creatifsoftware.filonova.model.Branch;

import java.io.Serializable;

/**
 * Created by kerembalaban on 7.04.2019 at 23:34.
 */
public class ContractInformation implements Serializable {
    public String contractId;
    public String contactId;
    public Branch dropoffBranch;
    public int segment;
    public long manuelPickupDateTimeStamp;
    public long manuelDropoffTimeStamp;
    public long PickupDateTimeStamp;
    public long DropoffTimeStamp;
    public boolean isManuelProcess;
    public String priceCodeId;

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
