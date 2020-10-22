package com.creatifsoftware.filonova.model.response;

import com.creatifsoftware.filonova.model.Branch;
import com.creatifsoftware.filonova.model.DamageDocument;
import com.creatifsoftware.filonova.model.DamageSize;
import com.creatifsoftware.filonova.model.DamageType;
import com.creatifsoftware.filonova.model.EquipmentPart;
import com.creatifsoftware.filonova.model.GroupCodeInformation;
import com.creatifsoftware.filonova.model.OptionSetItem;
import com.creatifsoftware.filonova.model.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kerembalaban on 7.04.2019 at 23:36.
 */
public class MasterDataResponse extends BaseResponse implements Serializable {
    public List<EquipmentPart> equipmentPartList;
    public List<DamageType> damageTypeList;
    public List<DamageSize> damageSizeList;
    public List<DamageDocument> damageDocumentList;
    public List<Branch> branchList;
    public List<GroupCodeInformation> groupCodeList;
    public List<OptionSetItem> equipmentStatusOptionSetList;
}
